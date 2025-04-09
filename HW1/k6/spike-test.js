import http from 'k6/http';
import { check, group } from 'k6';
import { btoa } from 'k6/encoding';

const BASE_URL = 'http://localhost:8080';
const CREDENTIALS = 'teste:teste';
const authHeader = {
    'Authorization': `Basic ${btoa(CREDENTIALS)}`,
    'Content-Type': 'application/json'
};

export const options = {
    vus: 1,
    iterations: 1,
    thresholds: {
        checks: ['rate > 0.8']
    }
};

export default function () {
    group('Restaurant API Tests', () => {
        // Create Restaurant
        const restaurantPayload = {
            name: `Restaurant_${Date.now()}`,
            location: "Test Location",
            seats: 50
        };

        const createRes = http.post(`${BASE_URL}/api/v1/restaurant`, JSON.stringify(restaurantPayload), {
            headers: { 'Content-Type': 'application/json' }
        });

        check(createRes, {
            'Create Restaurant - 201 Created': (r) => r.status === 201
        });

        const restaurantId = createRes.json('id');

        // Get Restaurant by ID
        const getByIdRes = http.get(`${BASE_URL}/api/v1/restaurant/${restaurantId}`);
        check(getByIdRes, {
            'Get Restaurant by ID - 200 OK': (r) => r.status === 200
        });

        // Update Restaurant
        const updatePayload = { ...restaurantPayload, seats: 60 };
        const updateRes = http.put(`${BASE_URL}/api/v1/restaurant`, JSON.stringify(updatePayload), {
            headers: { 'Content-Type': 'application/json' }
        });
        check(updateRes, {
            'Update Restaurant - 200 OK': (r) => r.status === 200
        });

        // Delete Restaurant
        const deleteRes = http.del(`${BASE_URL}/api/v1/restaurant/${restaurantId}`);
        check(deleteRes, {
            'Delete Restaurant - 204 No Content': (r) => r.status === 204
        });
    });

    group('Meal API Tests', () => {
        // First create a restaurant
        const restaurantRes = http.post(`${BASE_URL}/api/v1/restaurant`, JSON.stringify({
            name: `MealTestRestaurant_${Date.now()}`,
            location: "Meal Test Location",
            seats: 30
        }), { headers: { 'Content-Type': 'application/json' } });
        const restaurantId = restaurantRes.json('id');

        // Create Meal
        const mealPayload = {
            restaurant: { id: restaurantId },
            price: 15.99,
            date: new Date().toISOString(),
            name: `Meal_${Date.now()}`
        };

        const createMealRes = http.post(`${BASE_URL}/api/v1/meals`, JSON.stringify(mealPayload), {
            headers: { 'Content-Type': 'application/json' }
        });
        check(createMealRes, {
            'Create Meal - 201 Created': (r) => r.status === 201
        });

        const mealId = createMealRes.json('id');

        // Get Meal by ID
        const getMealRes = http.get(`${BASE_URL}/api/v1/meals/${mealId}`);
        check(getMealRes, {
            'Get Meal by ID - 200 OK': (r) => r.status === 200
        });

        // Cleanup
        http.del(`${BASE_URL}/api/v1/meals/${mealId}`);
        http.del(`${BASE_URL}/api/v1/restaurant/${restaurantId}`);
    });

    group('User API Tests', () => {
        // Create User
        const userPayload = {
            username: `user_${Date.now()}`,
            password: "testpassword",
            role: "USER"
        };

        const createRes = http.post(`${BASE_URL}/api/v1/users`, JSON.stringify(userPayload), {
            headers: authHeader
        });
        check(createRes, {
            'Create User - 201 Created': (r) => r.status === 201
        });

        const userId = createRes.json('id');

        // Get User by ID
        const getUserRes = http.get(`${BASE_URL}/api/private/v1/users/${userId}`, {
            headers: authHeader
        });
        check(getUserRes, {
            'Get User by ID - 200 OK': (r) => r.status === 200
        });

        // Update User
        const updatePayload = { ...userPayload, role: "STAFF" };
        const updateRes = http.put(`${BASE_URL}/api/private/v1/users`, JSON.stringify(updatePayload), {
            headers: authHeader
        });
        check(updateRes, {
            'Update User - 200 OK': (r) => r.status === 200
        });

        // Delete User
        const deleteRes = http.del(`${BASE_URL}/api/private/v1/users/${userId}`, null, {
            headers: authHeader
        });
        check(deleteRes, {
            'Delete User - 204 No Content': (r) => r.status === 204
        });
    });

    group('UserMeal API Tests', () => {
        // Create dependencies
        const restaurantRes = http.post(`${BASE_URL}/api/v1/restaurant`, JSON.stringify({
            name: `UMRestaurant_${Date.now()}`,
            location: "UM Test Loc",
            seats: 40
        }), { headers: { 'Content-Type': 'application/json' } });
        const restaurantId = restaurantRes.json('id');

        const mealRes = http.post(`${BASE_URL}/api/v1/meals`, JSON.stringify({
            restaurant: { id: restaurantId },
            price: 12.99,
            date: new Date().toISOString(),
            name: `UM_Meal_${Date.now()}`
        }), { headers: { 'Content-Type': 'application/json' } });
        const mealId = mealRes.json('id');

        const userRes = http.post(`${BASE_URL}/api/v1/users`, JSON.stringify({
            username: `um_user_${Date.now()}`,
            password: "testpass",
            role: "USER"
        }), { headers: authHeader });
        const userId = userRes.json('id');

        // Create UserMeal
        const userMealPayload = {
            user: { id: userId },
            meal: { id: mealId },
            isCheck: false,
            code: "TEST123"
        };

        const createUMRes = http.post(`${BASE_URL}/api/private/v1/user-meal`, JSON.stringify(userMealPayload), {
            headers: authHeader
        });
        check(createUMRes, {
            'Create UserMeal - 201 Created': (r) => r.status === 201
        });

        const userMealId = createUMRes.json('id');

        // Get UserMeal by ID
        const getUMRes = http.get(`${BASE_URL}/api/private/v1/user-meal/${userMealId}`, {
            headers: authHeader
        });
        check(getUMRes, {
            'Get UserMeal by ID - 200 OK': (r) => r.status === 200
        });

        // Cleanup
        http.del(`${BASE_URL}/api/private/v1/user-meal/${userMealId}`, null, { headers: authHeader });
        http.del(`${BASE_URL}/api/v1/meals/${mealId}`);
        http.del(`${BASE_URL}/api/v1/restaurant/${restaurantId}`);
        http.del(`${BASE_URL}/api/private/v1/users/${userId}`, null, { headers: authHeader });
    });

    // Additional tests
    group('Miscellaneous Tests', () => {
        // Test cache health endpoint
        const cacheHealthRes = http.get(`${BASE_URL}/api/v1/cache/health`);
        check(cacheHealthRes, {
            'Cache Health Check - 200 OK': (r) => r.status === 200
        });

        // Test get all restaurants
        const allRestaurantsRes = http.get(`${BASE_URL}/api/v1/restaurant/all`);
        check(allRestaurantsRes, {
            'Get All Restaurants - 200 OK': (r) => r.status === 200
        });
    });
}