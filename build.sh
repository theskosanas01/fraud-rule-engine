#!/bin/bash

# Fraud Rule Engine - Build Script
# Run this script to build the project

set -e  # Exit on any error

echo "======================================"
echo "Building Fraud Rule Engine Service"
echo "======================================"
echo ""

# Check Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed"
    exit 1
fi

# Check Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed"
    exit 1
fi

# Display versions
echo "Build Environment:"
java -version
echo ""
mvn -version
echo ""

# Clean and build
echo "Running: mvn clean package -DskipTests"
echo ""
mvn clean package -DskipTests

echo ""
echo "======================================"
echo "Build completed successfully!"
echo "======================================"
echo ""
echo "JAR file location: target/fraud-rule-engine-1.0.0.jar"
echo ""
echo "To run the application:"
echo "  java -jar target/fraud-rule-engine-1.0.0.jar"
echo ""
echo "Or use Maven:"
echo "  mvn spring-boot:run"
echo ""
