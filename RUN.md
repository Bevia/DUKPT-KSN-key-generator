## Verify the file exist:
find . -name "*.jar"

## Check where you jar file is:
ls -l build/libs/

### for example: (for this project)
DUKPT-KSN-key-generator-0.0.1-SNAPSHOT-boot.jar

## Clean and rebuild
./gradlew clean bootJar

## Run it
java -jar build/libs/DUKPT-KSN-key-generator-0.0.1-SNAPSHOT-boot.jar --simulate
java -jar build/libs/DUKPT-KSN-key-generator-0.0.1-SNAPSHOT-boot.jar

### or

java -jar build/libs/DUKPT-KSN-key-generator-0.0.1-SNAPSHOT-boot.jar -k1 0123456789ABCDEF0123456789ABCDEF -k2 FEDCBA9876543210FEDCBA9876543210 -k3 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00 -k 0123456789ABCDEF0123