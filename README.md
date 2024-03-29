# - Video Playlist Management REST API

This repository contains the implementation of REST APIs for managing video playlists, with a focus on playing video on demand (VOD) sources. The core functionalities include creating, reading, updating, and deleting playlists, as well as performing operations on individual playlist items. The API is built using Spring Boot and supports both Java and Kotlin.

# - Functionality Overview
The API allows users to create playlists containing VOD video sources, each with its own identifier, URL, download URL, thumbnail URL, and title. Users can specify a playback time span for each video source added to the playlist. Basic operations such as adding, removing, and reordering items in the playlist are supported, along with updating the playlist name and individual item details.

# - Architecture Choice: Three-Tier Architecture
The adoption of the three-tier architecture for this project stems from its inherent simplicity and practicality. In considering the project's scope and requirements, it became evident that a straightforward approach would be most fitting. By leveraging the three-tier architecture, I aim to strike a balance between structural organisation and ease of implementation. This architectural decision facilitates clear separation of concerns while ensuring the development process remains manageable and focused.
This architecture divides the application into presentation, business logic, and data access layers, allowing for easier understanding, testing, and future modifications. By structuring the code in this manner, it becomes easier to manage the complexity of the application and accommodate changes in requirements or technologies.

# - Unit Tests
Unit testing plays a pivotal role in ensuring the reliability and maintainability of the codebase. In this project, comprehensive unit tests are written using Mockito and JUnit, residing in the test folder.

## - Testing Approach
Each unit test focuses on verifying the functionality of individual components in isolation, adhering to the principles of unit testing. Mock objects are employed to simulate external dependencies, enabling controlled testing environments and minimizing dependencies between tests.

## - Code Coverage
The project maintains a high standard of code coverage, with all essential components thoroughly tested. The Jacoco code coverage tool is utilized to measure the extent of code coverage achieved by the unit tests. A detailed Jacoco report is generated and can be accessed in the site folder within the target directory.
Command to generate Report:  mvn clean verify 

## - Continuous Integration
Unit tests are integrated into the project's continuous integration pipeline, ensuring that tests are automatically executed with each code change. This practice helps in identifying and addressing issues promptly, fostering a culture of quality and reliability.

## - Best Practices
Unit tests follow industry best practices, including clear and descriptive test method names, meaningful assertions, and proper setup and teardown procedures. Additionally, tests are organized into logical groupings corresponding to the components being tested, enhancing readability and maintainability.

## - Future Considerations
As the project evolves, unit tests will continue to play a crucial role in validating new features and preventing regressions. Regular review and refinement of unit tests will be conducted to adapt to changes in requirements and maintain optimal code quality.

# - Usage
To use the APIs, simply deploy the Spring Boot application and interact with the provided endpoints.

Thank you for considering this repository. If you have any questions or feedback, please don't hesitate to reach out.