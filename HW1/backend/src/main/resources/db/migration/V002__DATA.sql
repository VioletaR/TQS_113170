-- Insert Users
INSERT INTO "user" (username, password) VALUES ('violeta', 'violeta123!');
INSERT INTO "user" (username, password) VALUES ('ramos', 'ramos123!');

-- Insert Restaurants
INSERT INTO restaurant (name) VALUES ('Pasta Palace');
INSERT INTO restaurant (name) VALUES ('Burger Haven');
INSERT INTO restaurant (name) VALUES ('Sushi Spot');
INSERT INTO restaurant (name) VALUES ('Vegan Delights');
INSERT INTO restaurant (name) VALUES ('BBQ King');

-- Insert Meals
INSERT INTO meal (name) VALUES ('Spaghetti Carbonara');
INSERT INTO meal (name) VALUES ('Margherita Pizza');
INSERT INTO meal (name) VALUES ('Cheeseburger');
INSERT INTO meal (name) VALUES ('Chicken Nuggets');
INSERT INTO meal (name) VALUES ('Salmon Sushi');
INSERT INTO meal (name) VALUES ('Tuna Roll');
INSERT INTO meal (name) VALUES ('Avocado Salad');
INSERT INTO meal (name) VALUES ('Vegan Burger');
INSERT INTO meal (name) VALUES ('BBQ Ribs');
INSERT INTO meal (name) VALUES ('Grilled Chicken');
INSERT INTO meal (name) VALUES ('Caesar Salad');
INSERT INTO meal (name) VALUES ('French Fries');
INSERT INTO meal (name) VALUES ('Miso Soup');
INSERT INTO meal (name) VALUES ('Teriyaki Chicken');
INSERT INTO meal (name) VALUES ('Falafel Wrap');
INSERT INTO meal (name) VALUES ('Steak Sandwich');
INSERT INTO meal (name) VALUES ('Grilled Salmon');
INSERT INTO meal (name) VALUES ('Pepperoni Pizza');
INSERT INTO meal (name) VALUES ('Chocolate Cake');
INSERT INTO meal (name) VALUES ('Ice Cream Sundae');

-- Insert Meal-Restaurant Associations with Prices and Dates
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (1, 1, 12.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (2, 1, 10.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (3, 2, 8.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (4, 2, 6.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (5, 3, 15.00, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (6, 3, 13.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (7, 4, 9.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (8, 4, 11.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (9, 5, 18.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (10, 5, 16.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (11, 1, 8.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (12, 2, 4.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (13, 3, 5.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (14, 3, 14.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (15, 4, 10.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (16, 5, 13.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (17, 5, 17.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (18, 1, 9.50, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (19, 2, 6.99, '2024-03-01');
INSERT INTO meal_restaurant (meal_id, restaurant_id, price, date) VALUES (20, 4, 7.99, '2024-03-01');

-- Insert User-Meal-Restaurant Associations
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (1, 1);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (1, 3);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (1, 5);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (1, 7);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (1, 9);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (2, 2);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (2, 4);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (2, 6);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (2, 8);
INSERT INTO user_meal_restaurant (user_id, meal_restaurant_id) VALUES (2, 10);
