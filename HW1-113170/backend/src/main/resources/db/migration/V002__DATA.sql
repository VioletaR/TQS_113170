INSERT INTO "user" (username, password, role) VALUES
                                                  ('violeta', 'violeta123!', 'USER'),
                                                  ('ramos', 'ramos123!', 'USER'),
                                                  ('admin', 'admin123!', 'STAFF'),
                                                  ('teste','teste','USER');

INSERT INTO restaurant (name, seats, location) VALUES
                                                   ('Pasta Palace', 20, 'Aveiro'),
                                                   ('Burger Haven', 30, 'Águeda'),
                                                   ('Sushi Spot', 25, 'Águeda'),
                                                   ('Vegan Delights', 15, 'Aveiro'),
                                                   ('BBQ King', 40, 'Aveiro'),
                                                    ('Pizza Paradise', 50, 'Aveiro'),
                                                    ('Steak House', 35, 'Águeda'),
                                                    ('Seafood Shack', 20, 'Aveiro'),
                                                    ('Taco Town', 30, 'Águeda'),
                                                    ('Dessert Den', 4, 'Águeda');

INSERT INTO meal (restaurant_id, name, price, meal_date) VALUES
                                                        (1, 'Spaghetti Carbonara', 12.99, '2025-04-10 12:00:00'),
                                                        (1, 'Margherita Pizza', 10.50, '2025-04-09 12:30:00'),
                                                        (2, 'Cheeseburger', 8.99, '2025-04-10 13:00:00'),
                                                        (2, 'Chicken Nuggets', 6.50, '2025-04-09 13:30:00'),
                                                        (3, 'Salmon Sushi', 15.00, '2025-04-09 14:00:00'),
                                                        (3, 'Tuna Roll', 13.50, '2025-04-09 14:30:00'),
                                                        (4, 'Avocado Salad', 9.99, '2025-04-09 15:00:00'),
                                                        (4, 'Vegan Burger', 11.50, '2025-04-09 15:30:00'),
                                                        (5, 'BBQ Ribs', 18.99, '2025-04-09 16:00:00'),
                                                        (5, 'Grilled Chicken', 16.50, '2025-04-09 16:30:00'),
                                                        (6, 'Margherita Pizza', 10.50, '2025-04-09 17:00:00'),
                                                        (6, 'Pepperoni Pizza', 11.50, '2025-04-09 17:30:00'),
                                                        (7, 'Ribeye Steak', 25.00, '2025-04-09 18:00:00'),
                                                        (7, 'Filet Mignon', 30.00, '2025-04-09 18:30:00'),
                                                        (8, 'Grilled Salmon', 20.00, '2025-04-09 19:00:00'),
                                                        (8, 'Shrimp Tacos', 15.50, '2025-04-09 19:30:00'),
                                                        (9, 'Chocolate Cake', 6.99, '2025-04-09 20:00:00'),
                                                        (9, 'Ice Cream Sundae', 4.50, '2025-04-09 20:30:00'),
                                                        (10,'Fruit Salad', 5.99, '2025-04-09 21:00:00'),
                                                        (10,'Cheesecake', 7.50, '2025-04-09 21:30:00');

INSERT INTO user_meal (user_id, meal_id, is_check, code) VALUES
                                                             (1, 1, TRUE, 'CUM-12345678'),
                                                             (1, 3, FALSE, 'CUM-a2b45678'),
                                                             (1, 5, TRUE, 'CUM-12cd5678'),
                                                             (1, 7, FALSE, 'CUM-1qw45678'),
                                                             (1, 9, TRUE, 'CUM-12io5678'),
                                                             (2, 2, FALSE, 'CUM-98345678'),
                                                             (2, 4, TRUE, 'CUM-1po45678'),
                                                             (2, 6, FALSE, 'CUM-12qw5678'),
                                                             (2, 8, TRUE, 'CUM-ae345678'),
                                                             (2, 10, FALSE, 'CUM-d23d5f78'),
                                                             (1, 20, TRUE, 'CUM-d2d4f6a8'),
                                                             (2, 20, FALSE, 'CUM-92lf5678'),
                                                             (3, 20, TRUE, 'CUM-p23uiy78');