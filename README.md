# Video Playlist Management REST API

This repository contains the implementation of REST APIs for managing video playlists, with a focus on playing video on demand (VOD) sources. The core functionalities include creating, reading, updating, and deleting playlists, as well as performing operations on individual playlist items. The API is built using Spring Boot and supports both Java and Kotlin.

# Functionality Overview
The API allows users to create playlists containing VOD video sources, each with its own identifier, URL, download URL, thumbnail URL, and title. Users can specify a playback time span for each video source added to the playlist. Basic operations such as adding, removing, and reordering items in the playlist are supported, along with updating the playlist name and individual item details.

# Architecture Choice: Three-Tier Architecture
The adoption of the three-tier architecture for this project stems from its inherent simplicity and practicality. In considering the project's scope and requirements, it became evident that a straightforward approach would be most fitting. By leveraging the three-tier architecture, I aim to strike a balance between structural organisation and ease of implementation. This architectural decision facilitates clear separation of concerns while ensuring the development process remains manageable and focused.
This architecture divides the application into presentation, business logic, and data access layers, allowing for easier understanding, testing, and future modifications. By structuring the code in this manner, it becomes easier to manage the complexity of the application and accommodate changes in requirements or technologies.

# Usage
To use the APIs, simply deploy the Spring Boot application and interact with the provided endpoints.

Thank you for considering this repository. If you have any questions or feedback, please don't hesitate to reach out.