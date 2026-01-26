# Database Schema

This schema is designed for a recipe web app with structured, normalized tables to support user authentication, recipe management, meal planners, shopping lists, sharing, and image storage. All IDs are UUIDs for scalability and security. Timestamps are in UTC.

---

## users
| Column        | Type          | Constraints                | Description                |
|-------------- |--------------|----------------------------|----------------------------|
| id            | UUID         | PK, NOT NULL               | User ID                    |
| name          | VARCHAR(100) | NOT NULL                   | User's display name        |
| email         | VARCHAR(255) | UNIQUE, NOT NULL           | User's email address       |
| password_hash | VARCHAR(255) | NOT NULL                   | Hashed password            |
| profile_info  | JSONB        |                            | Additional profile fields  |
| created_at    | TIMESTAMP    | NOT NULL, DEFAULT now()    | Account creation time      |
| updated_at    | TIMESTAMP    | NOT NULL, DEFAULT now()    | Last update time           |

---

## recipes
| Column        | Type          | Constraints                | Description                |
|-------------- |--------------|----------------------------|----------------------------|
| id            | UUID         | PK, NOT NULL               | Recipe ID                  |
| owner_id      | UUID         | FK users(id), NOT NULL     | Recipe owner               |
| title         | VARCHAR(200) | NOT NULL                   | Recipe title               |
| steps         | TEXT         | NOT NULL                   | Preparation steps          |
| nutrition_info| JSONB        |                            | Nutrition information      |
| visibility    | VARCHAR(10)  | NOT NULL, DEFAULT 'public' | public/private             |
| created_at    | TIMESTAMP    | NOT NULL, DEFAULT now()    | Creation time              |
| updated_at    | TIMESTAMP    | NOT NULL, DEFAULT now()    | Last update time           |

---

## recipe_ingredients
| Column      | Type         | Constraints                  | Description                        |
|-------------|--------------|------------------------------|-------------------------------------|
| id          | UUID         | PK, NOT NULL                 | Ingredient row ID                  |
| recipe_id   | UUID         | FK recipes(id), NOT NULL     | Linked recipe                      |
| name        | VARCHAR(100) | NOT NULL                     | Ingredient name (e.g., onion)      |
| amount      | NUMERIC(8,2) | NOT NULL                     | Quantity (e.g., 2, 50, 30)         |
| unit        | VARCHAR(20)  |                              | Unit (e.g., g, ml, pieces)         |

---

## recipe_images
| Column      | Type         | Constraints                  | Description                        |
|-------------|--------------|------------------------------|-------------------------------------|
| id          | UUID         | PK, NOT NULL                 | Image row ID                       |
| recipe_id   | UUID         | FK recipes(id), NOT NULL     | Linked recipe                      |
| url         | VARCHAR(512) | NOT NULL                     | S3 image URL                       |
| alt_text    | VARCHAR(255) |                              | Alt text for accessibility         |

---

## recipe_tags
| Column      | Type         | Constraints                  | Description                        |
|-------------|--------------|------------------------------|-------------------------------------|
| recipe_id   | UUID         | FK recipes(id), NOT NULL     | Linked recipe                      |
| tag         | VARCHAR(50)  | NOT NULL                     | Tag (e.g., 'vegan')                |
| PRIMARY KEY | (recipe_id, tag)                           |                                     |

---

## meal_planners
| Column         | Type         | Constraints                | Description                |
|--------------- |-------------|----------------------------|----------------------------|
| id             | UUID        | PK, NOT NULL               | Meal planner ID            |
| owner_id       | UUID        | FK users(id), NOT NULL     | Owner                      |
| week_start     | DATE        | NOT NULL                   | Start of week              |
| name           | VARCHAR(100)|                            | Optional planner name      |
| created_at     | TIMESTAMP   | NOT NULL, DEFAULT now()    | Creation time              |
| updated_at     | TIMESTAMP   | NOT NULL, DEFAULT now()    | Last update time           |

---

## meal_planner_recipes
| Column          | Type         | Constraints                | Description                        |
|-----------------|--------------|----------------------------|-------------------------------------|
| id              | UUID         | PK, NOT NULL               | Row ID                             |
| meal_planner_id | UUID         | FK meal_planners(id)       | Linked meal planner                |
| day_of_week     | VARCHAR(10)  | NOT NULL                   | e.g., 'Monday'                     |
| meal_type       | VARCHAR(20)  | NOT NULL                   | e.g., 'breakfast', 'dinner'        |
| recipe_id       | UUID         | FK recipes(id)             | Linked recipe                      |

---

## shopping_lists
| Column          | Type         | Constraints                | Description                        |
|-----------------|--------------|----------------------------|-------------------------------------|
| id              | UUID         | PK, NOT NULL               | Shopping list ID                   |
| owner_id        | UUID         | FK users(id), NOT NULL     | Owner                              |
| meal_planner_id | UUID         | FK meal_planners(id)       | Source meal planner                |
| created_at      | TIMESTAMP    | NOT NULL, DEFAULT now()    | Creation time                      |
| updated_at      | TIMESTAMP    | NOT NULL, DEFAULT now()    | Last update time                   |

---

## shopping_list_items
| Column           | Type         | Constraints                | Description                        |
|------------------|--------------|----------------------------|-------------------------------------|
| id               | UUID         | PK, NOT NULL               | Item ID                            |
| shopping_list_id | UUID         | FK shopping_lists(id)      | Linked shopping list               |
| name             | VARCHAR(100) | NOT NULL                   | Ingredient name                    |
| amount           | NUMERIC(8,2) |                            | Quantity                           |
| unit             | VARCHAR(20)  |                            | Unit                               |
| checked          | BOOLEAN      | NOT NULL, DEFAULT FALSE    | Marked as purchased                |

---

## shares
| Column      | Type         | Constraints                  | Description                        |
|-------------|--------------|------------------------------|-------------------------------------|
| id          | UUID         | PK, NOT NULL                 | Share ID                           |
| type        | VARCHAR(20)  | NOT NULL                     | 'recipe' or 'meal_planner'         |
| entity_id   | UUID         | NOT NULL                     | ID of shared entity                |
| public_url  | VARCHAR(255) | UNIQUE, NOT NULL             | Public share link                  |
| created_at  | TIMESTAMP    | NOT NULL, DEFAULT now()      | Creation time                      |

---

**Notes:**
- All foreign keys are indexed.
- Use `UUID` for IDs for scalability and security.
- `JSONB` fields allow flexible storage for nutrition and profile info.
- All timestamps are in UTC.
- S3 URLs are stored in `recipe_images`.
- Tagging, sharing, and visibility are normalized for flexibility.
- Ingredient and shopping list quantities are structured for consolidation and automation.

