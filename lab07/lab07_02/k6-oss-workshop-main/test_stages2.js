import http from 'k6/http';
import { check } from 'k6';

const VUs = 120; // 20 -> 120

const BASE_URL = __ENV.BASE_URL || 'http://localhost:3333';

export const options = {
    stages: [
        // ramp up from 0 to VUs over the next 30s seconds
        { duration: '30s', target: VUs },
        // run VUs over the next 30 seconds
        { duration: '30s', target: VUs },
        // ramp down from VUs to 0 VUs over the next 30 seconds
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        // SLO: 95% of requests complete in < 1.1s
        http_req_duration: ["p(95)<1100"],
        // SLO: HTTP failure rate < 1%
        http_req_failed: ["rate<0.01"],
        // SLO: At least 98% of checks pass (status 200 + body size <1KB)
        checks: ["rate > 0.98"],
    },
};

export default function () {
    const restrictions = {
        maxCaloriesPerSlice: 500,
        mustBeVegetarian: false,
        excludedIngredients: ['pepperoni'],
        excludedTools: ['knife'],
        maxNumberOfToppings: 6,
        minNumberOfToppings: 2,
    };

    const res = http.post(
        `${BASE_URL}/api/pizza`,
        JSON.stringify(restrictions),
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: 'token abcdef0123456789',
                'X-User-ID': 23423,
            },
        }
    );

    check(res, {
        'API response successful and body size <1KB': (r) =>
            r.status === 200 && r.body.length < 1024,
    });

    console.log(`${res.json().pizza.name} (${res.json().pizza.ingredients.length} ingredients)`);
}