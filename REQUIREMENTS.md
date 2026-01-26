# Recipe Web App Requirements

## 1. Introduction
- **Purpose:**  
  The main goal of the app is to provide a free-to-use collection of recipes for main meals. Users can browse different recipe cards and optionally select them to add to a weekly meal planner. Once the meal planner is complete, it will display all the selected recipes along with a consolidated list of ingredients needed for the week.
- **Target Users:**  
  People who are looking for a convenient way to generate a weekly meal plan and shopping list.

## 2. Functional Requirements
- **User Authentication**
  - Users must be able to register for a new account using email and password.
  - Users must be able to log in with their registered credentials.
  - Users must be able to log out securely.
  - Passwords must be stored securely (e.g., hashed and salted).
  - Users must be able to reset their password via email.
  - The system must prevent unauthorized access to user-specific features (e.g., adding recipes, managing meal planners).
  - Optionally, support for third-party authentication (e.g., Google, Apple) can be included.
- **View Recipes**
  - Users must be able to search for recipes by name, ingredient, or tag.
  - Users must be able to filter recipes by dietary preference (e.g., vegetarian, vegan, gluten-free).
  - Users must be able to filter recipes by cuisine type or meal type (e.g., breakfast, dinner).
  - Users must be able to sort recipes by popularity, rating, or preparation time.
  - Users must be able to view recipe details, including ingredients, preparation steps, nutrition information, and images.
  - The recipe list must support pagination or infinite scroll for large numbers of recipes.
  - The recipe viewing interface must be responsive and accessible on different devices.
  - The interface must support accessibility features (e.g., screen reader support, keyboard navigation).
  - Recipe search and filtering must be fast and efficient, even with large datasets.
- **Add/Edit/Delete Recipes**
  - Users must be able to add new recipes with required fields (title, ingredients, steps, etc.).
  - Users must be able to edit their own recipes.
  - Users must be able to delete their own recipes.
  - The system must validate recipe data before saving.
  - Optionally, users can upload images for recipes.
  - Optionally, users can mark recipes as private or public.
- **Create and Manage Meal Planners**
  - Users must be able to create a meal planner by selecting recipes for each day of the week.
  - Users must be able to view, edit, or delete their meal planners.
  - Users must be able to copy or reuse previous meal planners.
  - The system must prevent duplicate recipes for the same meal slot (optional).
  - Meal planners must be associated with the user who created them.
- **Generate and Manage Shopping Lists**
  - Users must be able to generate a shopping list based on their meal planner.
  - The shopping list must consolidate ingredients from all selected recipes, combining quantities where possible.
  - Users must be able to view, edit, or delete items from the shopping list.
  - Users must be able to mark items as purchased.
  - Optionally, users can export or print the shopping list.
- **Share Recipes or Meal Planners (Optional)**
  - Users must be able to share recipes or meal planners via a public link or social media.
  - Shared recipes/meal planners must be viewable without authentication.
  - Optionally, users can share directly with other registered users.
- **User Profile Management**
  - Users must be able to view and edit their profile information (e.g., name, email, password).
  - Users must be able to delete their account.
  - Users must be able to view their own recipes, meal planners, and shopping lists from their profile.

## 3. Non-Functional Requirements
- **Performance**
  - The app should load quickly and respond promptly to user actions.
  - Recipe search, filtering, and shopping list generation should be optimized for speed.
- **Security**
  - User data, including passwords and personal information, must be securely stored and transmitted.
  - The app must protect against common vulnerabilities (e.g., XSS, CSRF, SQL injection).
  - Only authenticated users can access and modify their own data.
- **Usability**
  - The interface should be intuitive and easy to navigate for all user types.
  - Clear feedback should be provided for user actions (e.g., saving, errors).
- **Accessibility**
  - The app must be usable with screen readers and keyboard navigation.
  - Color contrast and font sizes should meet accessibility standards.
- **Scalability**
  - The app should support a growing number of users and recipes without performance degradation.
  - The architecture should allow for future feature expansion.

## 4. User Stories / Use Cases
- As a user, I want to view recipes so I can find meal ideas.
- As a user, I want to add my own recipes.
- As a user, I want to create a meal planner for the week.
- As a user, I want to generate a shopping list from my meal planner.
- As a user, I want to edit or delete my recipes and meal planners.
- As a user, I want to share my recipes or meal planners with others.
- As a user, I want to manage my profile and account settings.

## 5. Data Model
- **Recipe:** title, ingredients, steps, nutrition info, images, tags, owner, visibility (public/private)
- **Meal Planner:** week, days, selected recipes per day, owner
- **Shopping List:** items, quantities, checked status, associated meal planner, owner
- **User:** name, email, password (hashed), profile info, recipes, meal planners, shopping lists

## 6. Wireframes / UI Mockups
- [Add wireframes or UI sketches here if available]

## 7. Sample Architecture

```mermaid
flowchart TD
    A[User Browser]
    B[React Frontend (AWS Amplify/S3 + CloudFront)]
    C[Spring Boot REST API (AWS Elastic Beanstalk/ECS)]
    D[AWS RDS (Database)]
    E[AWS S3 (Image/File Storage)]
    F[AWS SES (Email Service)]

    A --> B
    B --> C
    C --> D
    C --> E
    C --> F
```

## 8. Detailed Tech Stack Breakdown

**Frontend**
- React + TypeScript
- Material-UI or Chakra UI
- React Router
- Axios (for API calls)
- Hosted on AWS Amplify or S3 + CloudFront

**Backend**
- Java 25 with Spring Boot
- Spring Security (authentication, password hashing, JWT)
- Spring Data JPA (ORM for PostgreSQL/MySQL)
- JavaMail (for password reset)
- Spring Web (RESTful API)
- Spring Validation (input validation)
- Spring Boot Starter Test (JUnit, Mockito)
- Hosted on AWS Elastic Beanstalk or ECS

**Database**
- AWS RDS (PostgreSQL)

**File/Image Storage**
- AWS S3 (for recipe images and uploads)

**Other AWS Services**
- AWS CloudFront (CDN)
- AWS SES (for transactional emails, e.g., password reset)
- AWS IAM (for secure access control)

## 9. Example REST API Endpoints

- `POST /api/auth/register` — Register user
- `POST /api/auth/login` — Login user
- `POST /api/auth/reset-password` — Request password reset
- `GET /api/recipes` — List/search/filter recipes
- `POST /api/recipes` — Add recipe
- `PUT /api/recipes/{id}` — Edit recipe
- `DELETE /api/recipes/{id}` — Delete recipe
- `POST /api/meal-planners` — Create meal planner
- `GET /api/meal-planners` — List user’s meal planners
- `PUT /api/meal-planners/{id}` — Edit meal planner
- `DELETE /api/meal-planners/{id}` — Delete meal planner
- `POST /api/shopping-lists` — Generate shopping list from meal planner
- `GET /api/shopping-lists` — List shopping lists
- `PUT /api/shopping-lists/{id}` — Edit shopping list
- `DELETE /api/shopping-lists/{id}` — Delete shopping list
- `POST /api/recipes/{id}/share` — Share recipe (generate public link)
- `POST /api/meal-planners/{id}/share` — Share meal planner

## 10. Security & Best Practices

- Use HTTPS for all communications
- Store passwords securely (bcrypt)
- Use JWT for stateless authentication
- Validate and sanitize all user input
- Restrict S3 access via IAM roles
- Regularly update dependencies and monitor for vulnerabilities
