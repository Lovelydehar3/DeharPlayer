# DeharPlayer Release APK Validation Script (Windows)
# This script performs comprehensive security and build validation

param(
    [Switch]$Verbose = $false
)

$ErrorActionPreference = "Stop"

# Colors
$Green = "`e[32m"
$Red = "`e[31m"
$Yellow = "`e[33m"
$Reset = "`e[0m"

# Functions
function LogSuccess($message) {
    Write-Host "${Green}✓ $message${Reset}"
}

function LogError($message) {
    Write-Host "${Red}✗ $message${Reset}"
}

function LogWarning($message) {
    Write-Host "${Yellow}! $message${Reset}"
}

function LogInfo($message) {
    Write-Host $message
}

# ============================================================================
# 1. BUILD CONFIGURATION VALIDATION
# ============================================================================

Write-Host ""
Write-Host "1. Build Configuration Validation"
Write-Host "-----------------------------------"

$buildGradleFile = "app/build.gradle.kts"

if (Test-Path $buildGradleFile) {
    LogSuccess "build.gradle.kts found"
    
    $content = Get-Content $buildGradleFile -Raw
    
    # Verify minifyEnabled is true
    if ($content -match "isMinifyEnabled\s*=\s*true") {
        LogSuccess "ProGuard minification enabled in release build"
    } else {
        LogError "ProGuard minification NOT enabled"
    }
    
    # Verify debuggable is false
    if ($content -match "isDebuggable\s*=\s*false") {
        LogSuccess "Debuggable flag set to false for release build"
    } else {
        LogWarning "Debuggable flag may not be explicitly set to false"
    }
    
    # Verify shrinkResources is true
    if ($content -match "isShrinkResources\s*=\s*true") {
        LogSuccess "Resource shrinking enabled"
    } else {
        LogWarning "Resource shrinking may not be enabled"
    }
} else {
    LogError "build.gradle.kts not found"
}

# ============================================================================
# 2. PROGUARD RULES VALIDATION
# ============================================================================

Write-Host ""
Write-Host "2. ProGuard Rules Validation"
Write-Host "----------------------------"

$proguardFile = "app/proguard-rules.pro"

if (Test-Path $proguardFile) {
    LogSuccess "proguard-rules.pro found"
    
    $proguardContent = Get-Content $proguardFile -Raw
    
    # Check for critical rules
    if ($proguardContent -match "com\.dehar\.player") {
        LogSuccess "DeharPlayer core rules included"
    } else {
        LogError "DeharPlayer core rules not found"
    }
    
    if ($proguardContent -match "androidx\.compose") {
        LogSuccess "Jetpack Compose rules included"
    } else {
        LogError "Jetpack Compose rules not found"
    }
    
    if ($proguardContent -match "dagger\.hilt") {
        LogSuccess "Hilt DI rules included"
    } else {
        LogError "Hilt DI rules not found"
    }
    
    # Count total rules
    $ruleCount = ($proguardContent | Select-String "^-keep" -AllMatches).Matches.Count
    LogInfo "  Total -keep rules: $ruleCount"
    
} else {
    LogError "proguard-rules.pro not found"
}

# ============================================================================
# 3. MANIFEST VALIDATION
# ============================================================================

Write-Host ""
Write-Host "3. AndroidManifest.xml Validation"
Write-Host "----------------------------------"

$manifestFile = "app/src/main/AndroidManifest.xml"

if (Test-Path $manifestFile) {
    LogSuccess "AndroidManifest.xml found"
    
    $manifestContent = Get-Content $manifestFile -Raw
    
    # Check for debuggable=true
    if ($manifestContent -match 'android:debuggable\s*=\s*"true"') {
        LogError "SECURITY ISSUE: debuggable=`"true`" found in manifest"
    } else {
        LogSuccess "No debuggable=`"true`" in manifest"
    }
    
    # Check for hardcoded URLs that might be sensitive
    if ($manifestContent -match "://.*?(password|secret|key|token|api)" -and $manifestContent -match '=".*?"') {
        LogWarning "Potential hardcoded sensitive URLs detected"
    } else {
        LogSuccess "No obvious hardcoded sensitive URLs"
    }
    
    # Check for application tag
    if ($manifestContent -match "<application") {
        LogSuccess "Application tag present"
    } else {
        LogError "Application tag not found"
    }
} else {
    LogWarning "AndroidManifest.xml not found (may be generated)"
}

# ============================================================================
# 4. SOURCE CODE SECURITY SCAN
# ============================================================================

Write-Host ""
Write-Host "4. Source Code Security Scan"
Write-Host "-----------------------------"

$ktFiles = Get-ChildItem -Path "app/src" -Include "*.kt" -Recurse -ErrorAction SilentlyContinue
$suspiciousFiles = @()

foreach ($file in $ktFiles) {
    $fileContent = Get-Content $file -Raw -ErrorAction SilentlyContinue
    
    if ($fileContent -match "(API_KEY|SECRET_KEY|PASSWORD|PRIVATE_KEY)\s*=\s*`"") {
        $suspiciousFiles += $file.FullName
    }
}

if ($suspiciousFiles.Count -gt 0) {
    LogWarning "Potential hardcoded secrets found in:"
    $suspiciousFiles | ForEach-Object { LogWarning "  - $_" }
} else {
    LogSuccess "No obvious hardcoded secrets detected"
}

# ============================================================================
# 5. DEPENDENCY VALIDATION
# ============================================================================

Write-Host ""
Write-Host "5. Dependency Validation"
Write-Host "------------------------"

$libsVersionFile = "gradle/libs.versions.toml"

if (Test-Path $libsVersionFile) {
    LogSuccess "libs.versions.toml found"
    
    $libsContent = Get-Content $libsVersionFile -Raw
    
    if ($libsContent -match "androidx-compose\s*=\s*`"1\.[0-2]\.") {
        LogWarning "Consider updating Compose to latest version"
    } else {
        LogSuccess "Compose version appears up-to-date"
    }
} else {
    LogWarning "libs.versions.toml not found"
}

# ============================================================================
# 6. BUILD VERIFICATION CHECKLIST
# ============================================================================

Write-Host ""
Write-Host "6. Build Verification Checklist"
Write-Host "--------------------------------"

$checksPassed = 0
$checksTotal = 0

# Check 1: build.gradle.kts has minify enabled
$checksTotal++
$buildContent = Get-Content $buildGradleFile -Raw
if ($buildContent -match "isMinifyEnabled\s*=\s*true") {
    $checksPassed++
    LogSuccess "Check 1: ProGuard enabled"
} else {
    LogError "Check 1: ProGuard NOT enabled"
}

# Check 2: build.gradle.kts has debuggable false
$checksTotal++
if ($buildContent -match "isDebuggable\s*=\s*false") {
    $checksPassed++
    LogSuccess "Check 2: Debuggable disabled"
} else {
    LogWarning "Check 2: Debuggable may not be explicitly disabled"
}

# Check 3: proguard-rules.pro exists and is not empty
$checksTotal++
if ((Test-Path $proguardFile) -and (Get-Item $proguardFile).Length -gt 0) {
    $checksPassed++
    LogSuccess "Check 3: ProGuard rules configured"
} else {
    LogError "Check 3: ProGuard rules missing or empty"
}

# Check 4: Core modules exist
$checksTotal++
if ((Test-Path "core/ui") -and (Test-Path "core/data") -and (Test-Path "feature/music-player")) {
    $checksPassed++
    LogSuccess "Check 4: Core modules present"
} else {
    LogError "Check 4: Core modules missing"
}

# Check 5: All 19 feature modules exist
$checksTotal++
$featureCount = (Get-ChildItem -Path "feature" -Directory -ErrorAction SilentlyContinue).Count
if ($featureCount -ge 18) {
    $checksPassed++
    LogSuccess "Check 5: All feature modules present ($featureCount modules)"
} else {
    LogWarning "Check 5: Only $featureCount feature modules found"
}

Write-Host ""
Write-Host "=========================================="
Write-Host "VALIDATION SUMMARY"
Write-Host "=========================================="
LogInfo "Passed: $checksPassed / $checksTotal checks"

if ($checksPassed -eq $checksTotal) {
    LogSuccess "All checks passed! Ready for release build."
    exit 0
} else {
    LogWarning "Some checks did not pass. Review above."
    exit 1
}
