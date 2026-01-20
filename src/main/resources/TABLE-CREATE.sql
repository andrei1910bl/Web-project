CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('user', 'bartender', 'admin'))
);

CREATE TABLE cocktails (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
     author_id BIGINT UNSIGNED,
     CONSTRAINT fk_cocktail_author FOREIGN KEY (author_id)
     REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    cocktail_id BIGINT UNSIGNED NOT NULL,
    score INTEGER CHECK (score >= 1 AND score <= 5),
    UNIQUE KEY (user_id, cocktail_id),
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_cocktail FOREIGN KEY (cocktail_id) REFERENCES cocktails(id) ON DELETE CASCADE
);

CREATE TABLE cocktail_ingredients (
    cocktail_id BIGINT UNSIGNED NOT NULL,
    ingredient_id BIGINT UNSIGNED NOT NULL,
    amount VARCHAR(50),
    PRIMARY KEY (cocktail_id, ingredient_id),
    CONSTRAINT fk_ci_cocktail FOREIGN KEY (cocktail_id) REFERENCES cocktails(id) ON DELETE CASCADE,
    CONSTRAINT fk_ci_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);