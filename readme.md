# Playwright Sample Test Framework

This project sets up a test framework using **Maven** to run tests in parallel, along with **Smart Locator** configuration through the `locators.json` file. The framework allows tests to be executed efficiently and concurrently, improving the speed and effectiveness of your test suite.


## Clone Project

To get started with the framework, follow these steps:

**Clone the Repository**  
   Clone the repository to your local machine:

   ```bash
   git clone https://github.com/zafer-barutcu/playwright-tests.git
   cd playwright-cucumber-framework-master
   ```

## Execute Tests in Parallel

Here is the necessary command for parallel testing. I managed in surefire-plugin.


   ```bash
  mvn test
   ```
## Manage Locators Wisely

I added a couple of steps for this implementation. You just have to implement your locators into `locators.json` file.



