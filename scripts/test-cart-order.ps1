# =============================================================================
# Test script for Cart & Order endpoints
#
# Covers:
#   - Registration + login
#   - Cart happy path (GET / POST / PATCH / DELETE item, DELETE cart)
#   - Cart validation & error cases (400 / 401 / 404)
#   - Order checkout flow (POST / GET / GET by id)
#   - Order error cases (empty cart, insufficient stock, ownership)
#
# Requirements:
#   - Backend running on http://localhost:8080
#   - ProductVariants pre-seeded: id 1 (stock 10), id 2 (stock 5), id 3 (stock 1)
# =============================================================================

$ErrorActionPreference = 'Stop'
$BaseUrl = 'http://localhost:8080'
$script:passed = 0
$script:failed = 0
$script:results = @()

function Invoke-Api {
    param(
        [string]$Method,
        [string]$Path,
        [hashtable]$Headers = @{},
        [object]$Body = $null
    )
    $uri = "$BaseUrl$Path"
    $params = @{
        Uri             = $uri
        Method          = $Method
        Headers         = $Headers
        UseBasicParsing = $true
        ErrorAction     = 'Stop'
    }
    if ($Body) {
        $params.Body = ($Body | ConvertTo-Json -Depth 5)
        $params.ContentType = 'application/json'
    }
    try {
        $r = Invoke-WebRequest @params
        return @{ Status = $r.StatusCode; Body = $r.Content }
    } catch {
        $resp = $_.Exception.Response
        if ($resp) {
            $status = [int]$resp.StatusCode
            $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
            $body = $reader.ReadToEnd()
            return @{ Status = $status; Body = $body }
        }
        throw
    }
}

function Assert-Case {
    param(
        [string]$Name,
        [int]$ExpectedStatus,
        [hashtable]$Result,
        [scriptblock]$ExtraCheck = $null
    )
    $ok = ($Result.Status -eq $ExpectedStatus)
    if ($ok -and $ExtraCheck) {
        try { $ok = & $ExtraCheck $Result } catch { $ok = $false }
    }
    if ($ok) {
        $script:passed++
        $line = "  [PASS] $Name  (HTTP $($Result.Status))"
        Write-Host $line -ForegroundColor Green
    } else {
        $script:failed++
        $line = "  [FAIL] $Name  (expected $ExpectedStatus, got $($Result.Status))`n         body: $($Result.Body)"
        Write-Host $line -ForegroundColor Red
    }
    $script:results += [PSCustomObject]@{
        Name           = $Name
        Expected       = $ExpectedStatus
        Actual         = $Result.Status
        Pass           = $ok
        ResponsePreview = if ($Result.Body.Length -gt 300) { $Result.Body.Substring(0, 300) + '...' } else { $Result.Body }
    }
}

function Section($title) {
    Write-Host "`n=== $title ===" -ForegroundColor Cyan
}

# ---------------------------------------------------------------------------
# 0. Setup: register a new buyer, login, capture token
# ---------------------------------------------------------------------------
Section 'Setup: register + login'

$suffix   = [DateTimeOffset]::Now.ToUnixTimeSeconds()
$username = "testbuyer$suffix"
$email    = "test+$suffix@example.com"
$password = 'Test1234'

$register = Invoke-Api POST '/auth/register' -Body @{
    email    = $email
    username = $username
    password = $password
    name     = 'Test'
    surname  = 'Buyer'
}
Assert-Case 'Register new BUYER' 200 $register

$login = Invoke-Api POST '/auth/login' -Body @{ email = $email; password = $password }
Assert-Case 'Login with new BUYER' 200 $login

$token = ($login.Body | ConvertFrom-Json).access_token
$auth  = @{ Authorization = "Bearer $token" }

# ---------------------------------------------------------------------------
# 1. Cart happy path
# ---------------------------------------------------------------------------
Section 'Cart: happy path'

$r = Invoke-Api GET '/cart' -Headers $auth
Assert-Case 'GET /cart (empty, auto-create)' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 0 }

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 1; quantity = 2 }
Assert-Case 'POST /cart/items variant=1 qty=2' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 1 }

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 1; quantity = 1 }
Assert-Case 'POST /cart/items same variant merges qty (expected 3)' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items[0].quantity) -eq 3 }

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 2; quantity = 1 }
Assert-Case 'POST /cart/items variant=2 (different variant)' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 2 }

$r = Invoke-Api GET '/cart' -Headers $auth
$cart = $r.Body | ConvertFrom-Json
$itemIdV1 = ($cart.items | Where-Object { $_.productVariantId -eq 1 }).id
$itemIdV2 = ($cart.items | Where-Object { $_.productVariantId -eq 2 }).id
Assert-Case "GET /cart totalAmount expected (19999*3 + 15999*1 = 75996)" 200 $r { param($x) (($x.Body | ConvertFrom-Json).totalAmount) -eq 75996 }

$r = Invoke-Api PATCH "/cart/items/$itemIdV1" -Headers $auth -Body @{ quantity = 5 }
Assert-Case 'PATCH /cart/items/{id} qty=5' 200 $r { param($x) ((($x.Body | ConvertFrom-Json).items | Where-Object { $_.id -eq $itemIdV1 }).quantity) -eq 5 }

$r = Invoke-Api DELETE "/cart/items/$itemIdV2" -Headers $auth
Assert-Case 'DELETE /cart/items/{id}' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 1 }

# ---------------------------------------------------------------------------
# 2. Cart error cases
# ---------------------------------------------------------------------------
Section 'Cart: error cases'

$r = Invoke-Api POST '/cart/items' -Body @{ productVariantId = 1; quantity = 1 }
Assert-Case 'POST /cart/items without JWT -> 403' 403 $r

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 999; quantity = 1 }
Assert-Case 'POST /cart/items with non-existent variant -> 404' 404 $r

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 1; quantity = 0 }
Assert-Case 'POST /cart/items quantity=0 -> 400 (validation)' 400 $r

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 1; quantity = -3 }
Assert-Case 'POST /cart/items quantity=-3 -> 400 (validation)' 400 $r

$r = Invoke-Api POST '/cart/items' -Headers $auth -Body @{}
Assert-Case 'POST /cart/items empty body -> 400 (validation)' 400 $r

$r = Invoke-Api PATCH '/cart/items/999999' -Headers $auth -Body @{ quantity = 2 }
Assert-Case 'PATCH non-existent cart item -> 404' 404 $r

$r = Invoke-Api DELETE '/cart/items/999999' -Headers $auth
Assert-Case 'DELETE non-existent cart item -> 404' 404 $r

# ---------------------------------------------------------------------------
# 3. Order happy path (checkout + list + detail)
# ---------------------------------------------------------------------------
Section 'Orders: happy path'

$r = Invoke-Api POST '/orders' -Headers $auth
Assert-Case 'POST /orders checkout (cart has items) -> 201' 201 $r { param($x) (($x.Body | ConvertFrom-Json).status) -eq 'PENDING' }
$orderId = ($r.Body | ConvertFrom-Json).id

$r = Invoke-Api GET '/cart' -Headers $auth
Assert-Case 'GET /cart after checkout is empty' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 0 }

$r = Invoke-Api GET '/orders' -Headers $auth
Assert-Case 'GET /orders list (>=1 order)' 200 $r { param($x) (($x.Body | ConvertFrom-Json).Count) -ge 1 }

$r = Invoke-Api GET "/orders/$orderId" -Headers $auth
Assert-Case 'GET /orders/{id} detail' 200 $r { param($x) (($x.Body | ConvertFrom-Json).id) -eq $orderId }

# ---------------------------------------------------------------------------
# 4. Order error cases
# ---------------------------------------------------------------------------
Section 'Orders: error cases'

$r = Invoke-Api POST '/orders' -Headers $auth
Assert-Case 'POST /orders with empty cart -> 400' 400 $r

$r = Invoke-Api GET '/orders/999999' -Headers $auth
Assert-Case 'GET /orders/{idInexistente} -> 404' 404 $r

# Insufficient stock: variant 3 only has stock=1
Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 3; quantity = 5 } | Out-Null
$r = Invoke-Api POST '/orders' -Headers $auth
Assert-Case 'POST /orders with qty > stock -> 409' 409 $r

# Clean up
Invoke-Api DELETE '/cart' -Headers $auth | Out-Null

# ---------------------------------------------------------------------------
# 5. Cart clear
# ---------------------------------------------------------------------------
Section 'Cart: clear'

Invoke-Api POST '/cart/items' -Headers $auth -Body @{ productVariantId = 2; quantity = 1 } | Out-Null
$r = Invoke-Api DELETE '/cart' -Headers $auth
Assert-Case 'DELETE /cart -> 204' 204 $r

$r = Invoke-Api GET '/cart' -Headers $auth
Assert-Case 'GET /cart after clear is empty' 200 $r { param($x) (($x.Body | ConvertFrom-Json).items.Count) -eq 0 }

# ---------------------------------------------------------------------------
# 6. Ownership: another user cannot see this user's order
# ---------------------------------------------------------------------------
Section 'Orders: ownership'

$suffix2 = [DateTimeOffset]::Now.ToUnixTimeSeconds() + 1
Invoke-Api POST '/auth/register' -Body @{
    email    = "other+$suffix2@example.com"
    username = "other$suffix2"
    password = $password
    name     = 'Other'
    surname  = 'User'
} | Out-Null
$login2 = Invoke-Api POST '/auth/login' -Body @{ email = "other+$suffix2@example.com"; password = $password }
$token2 = ($login2.Body | ConvertFrom-Json).access_token
$auth2  = @{ Authorization = "Bearer $token2" }

$r = Invoke-Api GET "/orders/$orderId" -Headers $auth2
Assert-Case "GET /orders/{otherUsersOrderId} -> 404 (ownership)" 404 $r

# ---------------------------------------------------------------------------
# Summary
# ---------------------------------------------------------------------------
Write-Host "`n=== Summary ===" -ForegroundColor Cyan
Write-Host "Passed: $($script:passed)" -ForegroundColor Green
Write-Host "Failed: $($script:failed)" -ForegroundColor $(if ($script:failed -eq 0) { 'Green' } else { 'Red' })

$script:results | Export-Csv -Path 'scripts/test-results.csv' -NoTypeInformation -Encoding UTF8
Write-Host "`nResults saved to scripts/test-results.csv"

if ($script:failed -gt 0) { exit 1 } else { exit 0 }
