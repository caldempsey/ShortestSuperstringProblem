BadNiecesIO Readme
=== 

Solution Requirements
---
* The directory specified by LOGS_DIR must exist and write permissions must be given in order for logs to be created.
* The file path provided as an argument must exist and be readable.
* The utils package must be present.

Main method stages.
---
This application executes in the following well-defined stages

 * Validation stage: Validates arguments passed to the application. Makes pre-runtime checks to ensure application safety.
 * Runtime stage: Runtime stage using passed information from arguments.

Exit Codes
---
 * Exit code 1. An error which occurs before the application runtime (i.e. validating arguments for type safety, or initialising the logging system) will result in a system exit status of 1. These errors occur on the validation stage of the application.
 * Exit code 2. A runtime error which occurs post validation will result in a system exit status of 2. These errors occur on the runtime stage of the application.
 * Exit code 0. Normal termination of the program.