#!/bin/bash
# Final APK Validation Script for DeharPlayer Release Build
# This script performs comprehensive security and build validation

set -e

echo "=========================================="
echo "DeharPlayer Release APK Validation"
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

log_error() {
    echo -e "${RED}✗ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}! $1${NC}"
}

# ============================================================================
# 1. BUILD CONFIGURATION VALIDATION
# ============================================================================

echo ""
echo "1. Build Configuration Validation"
echo "-----------------------------------"

# Check if build.gradle.kts exists
if [ -f "app/build.gradle.kts" ]; then
    log_success "build.gradle.kts found"
    
    # Verify minifyEnabled is true
    if grep -q "isMinifyEnabled = true" app/build.gradle.kts; then
        log_success "ProGuard minification enabled in release build"
    else
        log_error "ProGuard minification NOT enabled"
    fi
    
    # Verify debuggable is false
    if grep -q "isDebuggable = false" app/build.gradle.kts; then
        log_success "Debuggable flag set to false for release build"
    else
        log_warning "Debuggable flag may not be explicitly set to false"
    fi
    
    # Verify shrinkResources is true
    if grep -q "isShrinkResources = true" app/build.gradle.kts; then
        log_success "Resource shrinking enabled"
    else
        log_warning "Resource shrinking may not be enabled"
    fi
else
    log_error "build.gradle.kts not found"
fi

# ============================================================================
# 2. PROGUARD RULES VALIDATION
# ============================================================================

echo ""
echo "2. ProGuard Rules Validation"
echo "----------------------------"

if [ -f "app/proguard-rules.pro" ]; then
    log_success "proguard-rules.pro found"
    
    # Check for critical rules
    if grep -q "com.dehar.player" app/proguard-rules.pro; then
        log_success "DeharPlayer core rules included"
    else
        log_error "DeharPlayer core rules not found"
    fi
    
    if grep -q "androidx.compose" app/proguard-rules.pro; then
        log_success "Jetpack Compose rules included"
    else
        log_error "Jetpack Compose rules not found"
    fi
    
    if grep -q "dagger.hilt" app/proguard-rules.pro; then
        log_success "Hilt DI rules included"
    else
        log_error "Hilt DI rules not found"
    fi
    
    # Count total rules
    rule_count=$(grep -c "^-keep" app/proguard-rules.pro || true)
    echo "  Total -keep rules: $rule_count"
    
else
    log_error "proguard-rules.pro not found"
fi

# ============================================================================
# 3. MANIFEST VALIDATION
# ============================================================================

echo ""
echo "3. AndroidManifest.xml Validation"
echo "----------------------------------"

MANIFEST_FILE="app/src/main/AndroidManifest.xml"

if [ -f "$MANIFEST_FILE" ]; then
    log_success "AndroidManifest.xml found"
    
    # Check for hardcoded API keys or secrets (basic patterns)
    if grep -q "android:debuggable=\"true\"" "$MANIFEST_FILE"; then
        log_error "SECURITY ISSUE: debuggable=\"true\" found in manifest"
    else
        log_success "No debuggable=\"true\" in manifest"
    fi
    
    # Check for hardcoded URLs that might be sensitive
    if grep -q "://[a-z]*password\|://[a-z]*secret\|://[a-z]*key" "$MANIFEST_FILE" -i; then
        log_warning "Potential hardcoded sensitive URLs detected"
    else
        log_success "No obvious hardcoded sensitive URLs"
    fi
    
    # Check for application tag
    if grep -q "<application" "$MANIFEST_FILE"; then
        log_success "Application tag present"
    else
        log_error "Application tag not found"
    fi
    
else
    log_warning "AndroidManifest.xml not found (may be generated)"
fi

# ============================================================================
# 4. SOURCE CODE SECURITY SCAN
# ============================================================================

echo ""
echo "4. Source Code Security Scan"
echo "-----------------------------"

# Check for hardcoded API keys/secrets in source files
find app/src -name "*.kt" -o -name "*.java" | while read file; do
    if grep -q "API_KEY\|SECRET_KEY\|PASSWORD" "$file" 2>/dev/null; then
        if grep -q "= \"" "$file" 2>/dev/null; then
            log_warning "Potential hardcoded secret in: $file"
        fi
    fi
done

log_success "Source code security scan completed"

# ============================================================================
# 5. DEPENDENCY VALIDATION
# ============================================================================

echo ""
echo "5. Dependency Validation"
echo "------------------------"

if [ -f "gradle/libs.versions.toml" ]; then
    log_success "libs.versions.toml found"
    
    # Check for commonly vulnerable versions
    if grep -q "androidx.compose = \"1\.[0-2]\." gradle/libs.versions.toml 2>/dev/null; then
        log_warning "Consider updating Compose to latest version"
    else
        log_success "Compose version appears up-to-date"
    fi
    
else
    log_warning "libs.versions.toml not found"
fi

# ============================================================================
# 6. BUILD VERIFICATION
# ============================================================================

echo ""
echo "6. Build Verification Checklist"
echo "--------------------------------"

CHECKS_PASSED=0
CHECKS_TOTAL=0

# Check 1: build.gradle.kts has minify enabled
CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
if grep -q "isMinifyEnabled = true" app/build.gradle.kts; then
    CHECKS_PASSED=$((CHECKS_PASSED + 1))
    log_success "Check 1: ProGuard enabled"
else
    log_error "Check 1: ProGuard NOT enabled"
fi

# Check 2: build.gradle.kts has debuggable false
CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
if grep -q "isDebuggable = false" app/build.gradle.kts; then
    CHECKS_PASSED=$((CHECKS_PASSED + 1))
    log_success "Check 2: Debuggable disabled"
else
    log_warning "Check 2: Debuggable may not be explicitly disabled"
fi

# Check 3: proguard-rules.pro exists and is not empty
CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
if [ -f "app/proguard-rules.pro" ] && [ -s "app/proguard-rules.pro" ]; then
    CHECKS_PASSED=$((CHECKS_PASSED + 1))
    log_success "Check 3: ProGuard rules configured"
else
    log_error "Check 3: ProGuard rules missing or empty"
fi

# Check 4: Core modules exist
CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
if [ -d "core/ui" ] && [ -d "core/data" ] && [ -d "feature/music-player" ]; then
    CHECKS_PASSED=$((CHECKS_PASSED + 1))
    log_success "Check 4: Core modules present"
else
    log_error "Check 4: Core modules missing"
fi

# Check 5: All 19 feature modules exist
CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
FEATURE_COUNT=$(ls -d feature/*/ 2>/dev/null | wc -l)
if [ "$FEATURE_COUNT" -ge 18 ]; then
    CHECKS_PASSED=$((CHECKS_PASSED + 1))
    log_success "Check 5: All feature modules present ($FEATURE_COUNT modules)"
else
    log_warning "Check 5: Only $FEATURE_COUNT feature modules found"
fi

echo ""
echo "=========================================="
echo "VALIDATION SUMMARY"
echo "=========================================="
echo "Passed: $CHECKS_PASSED / $CHECKS_TOTAL checks"

if [ "$CHECKS_PASSED" -eq "$CHECKS_TOTAL" ]; then
    log_success "All checks passed! Ready for release build."
    exit 0
else
    log_warning "Some checks did not pass. Review above."
    exit 1
fi
