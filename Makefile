.PHONY: verify
vet:
	@cd microservices; ./gradlew verifyGoogleJavaFormat

.PHONY: fmt
fmt:
	@cd microservices; ./gradlew googleJavaFormat

.PHONY: test
test: vet
	@cd microservices; ./gradlew clean test
