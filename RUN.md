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


## To run simulation
	1.	Open Run → Edit Configurations
	2.	Add a new Application configuration
	3.	Set the Main class to org.corebaseit.dukptksnkeygenerator.DukptApplicationKt
	4.	Set Program arguments to: 
--simulate -k1 0123456789ABCDEF0123456789ABCDEF -k2 FEDCBA9876543210FEDCBA9876543210 -k3 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA00 -k FFFF9876543210E00000 --pan 4000000000000002 --pin 1234