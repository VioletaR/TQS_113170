INSERT INTO "user" ( username, password, role) VALUES
                                                  ('violeta', 'violeta123!', 'USER'),
                                                  ('ramos', 'ramos123!', 'USER'),
                                                  ('admin', 'admin123!', 'STAFF');

INSERT INTO restaurant (name, seats, district) VALUES
                                         ('Pasta Palace', 20, 'Aveiro'),
                                         ('Burger Haven', 30,'Aveiro'),
                                         ('Sushi Spot', 25,'Aveiro'),
                                         ('Vegan Delights', 15,'Aveiro'),
                                         ('BBQ King', 40,'Aveiro');

INSERT INTO meal (restaurant_id, meal, price, date) VALUES
                                                        (1, 'Spaghetti Carbonara', 12.99, '2025-03-03 12:00:00'),
                                                        (1, 'Margherita Pizza',   10.50, '2025-03-03 12:30:00'),
                                                        (2, 'Cheeseburger',        8.99, '2025-03-03 13:00:00'),
                                                        (2, 'Chicken Nuggets',     6.50, '2025-03-03 13:30:00'),
                                                        (3, 'Salmon Sushi',       15.00, '2025-03-03 14:00:00'),
                                                        (3, 'Tuna Roll',          13.50, '2025-03-03 14:30:00'),
                                                        (4, 'Avocado Salad',       9.99, '2025-03-03 15:00:00'),
                                                        (4, 'Vegan Burger',       11.50, '2025-03-03 15:30:00'),
                                                        (5, 'BBQ Ribs',          18.99, '2025-03-03 16:00:00'),
                                                        (5, 'Grilled Chicken',    16.50, '2025-03-03 16:30:00');


INSERT INTO user_meal (user_id, meal_id, is_check) VALUES
                                                       (1, 1, TRUE),
                                                       (1, 3, FALSE),
                                                       (1, 5, TRUE),
                                                       (1, 7, FALSE),
                                                       (1, 9, TRUE),
                                                       (2, 2, FALSE),
                                                       (2, 4, TRUE),
                                                       (2, 6, FALSE),
                                                       (2, 8, TRUE),
                                                       (2, 10, FALSE);
