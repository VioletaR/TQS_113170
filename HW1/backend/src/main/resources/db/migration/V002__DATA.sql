INSERT INTO "user" (username, password, role) VALUES
                                                  ('violeta', 'violeta123!', 'USER'),
                                                  ('ramos', 'ramos123!', 'USER'),
                                                  ('admin', 'admin123!', 'STAFF');

INSERT INTO restaurant (name, seats, location) VALUES
                                                   ('Pasta Palace', 20, 'Aveiro'),
                                                   ('Burger Haven', 30, 'Águeda'),
                                                   ('Sushi Spot', 25, 'Águeda'),
                                                   ('Vegan Delights', 15, 'Aveiro'),
                                                   ('BBQ King', 40, 'Aveiro');

INSERT INTO meal (restaurant_id, meal, price, date) VALUES
                                                        (1, 'Spaghetti Carbonara', 12.99, '2025-04-04 12:00:00'),
                                                        (1, 'Margherita Pizza', 10.50, '2025-04-04 12:30:00'),
                                                        (2, 'Cheeseburger', 8.99, '2025-04-04 13:00:00'),
                                                        (2, 'Chicken Nuggets', 6.50, '2025-04-04 13:30:00'),
                                                        (3, 'Salmon Sushi', 15.00, '2025-04-04 14:00:00'),
                                                        (3, 'Tuna Roll', 13.50, '2025-04-04 14:30:00'),
                                                        (4, 'Avocado Salad', 9.99, '2025-04-04 15:00:00'),
                                                        (4, 'Vegan Burger', 11.50, '2025-04-04 15:30:00'),
                                                        (5, 'BBQ Ribs', 18.99, '2025-04-04 16:00:00'),
                                                        (5, 'Grilled Chicken', 16.50, '2025-04-04 16:30:00');

INSERT INTO user_meal (user_id, meal_id, is_check, code) VALUES
                                                             (1, 1, TRUE, 'CUM-12345678'),
                                                             (1, 3, FALSE, 'CUM-12345678'),
                                                             (1, 5, TRUE, 'CUM-12345678'),
                                                             (1, 7, FALSE, 'CUM-12345678'),
                                                             (1, 9, TRUE, 'CUM-12345678'),
                                                             (2, 2, FALSE, 'CUM-12345678'),
                                                             (2, 4, TRUE, 'CUM-12345678'),
                                                             (2, 6, FALSE, 'CUM-12345678'),
                                                             (2, 8, TRUE, 'CUM-12345678'),
                                                             (2, 10, FALSE, 'CUM-12345678');