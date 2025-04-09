import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';


const restaurantResponseTime = new Trend('restaurant_response_time');
const errorRate = new Rate('error_rate');

export const options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '15s', target: 200 },
        { duration: '30s', target: 100 },
        { duration: '15s', target: 50 },
        { duration: '30s', target: 0 }
    ],
    thresholds: {

        'http_req_duration': ['p(95)<500', 'p(99)<1000'],
        'http_req_failed': ['rate<0.01'],
        'http_reqs': ['count>1000'],

        'restaurant_response_time': ['p(95)<500', 'p(99)<1000'],

        'error_rate': ['rate<0.01'],
    },
};

export function setup() {
    console.log("Starting setup phase...");


    const restaurantsRes = http.get('http://backend:8080/api/v1/restaurant/all');
    const restaurantsCheck = check(restaurantsRes, {
        'restaurants fetched': (r) => r.status === 200,
        'restaurants response time': (r) => r.timings.duration < 1000,
        'restaurants response has data': (r) => r.json().length > 0,
    });

    if (!restaurantsCheck) {
        throw new Error('Failed to fetch restaurants during setup');
    }

    const restaurants = restaurantsRes.json();
    const restaurantIds = restaurants.map((r) => r.id).filter(id => id !== undefined && id !== null);
    console.log(`Found ${restaurantIds.length} valid restaurant IDs`);


    if (restaurantIds.length === 0) {
        throw new Error('No valid restaurant IDs found during setup');
    }

    console.log("Setup completed successfully");
    return { restaurantIds };
}

function randomItem(array) {
    if (!array || array.length === 0) {
        throw new Error('Cannot get random item from empty array');
    }
    return array[Math.floor(Math.random() * array.length)];
}


export default function (data) {
    const { restaurantIds } = data;

    if (Math.random() < 0.5) {
        if (Math.random() < 0.5) {
            const res = http.get('http://backend:8080/api/v1/restaurant/all');
            restaurantResponseTime.add(res.timings.duration);
            const checkResult = check(res, {
                'get all restaurants status 200': (r) => r.status === 200,
                'get all restaurants response time': (r) => r.timings.duration < 1000,
                'get all restaurants has data': (r) => r.json().length > 0,
                'get all restaurants valid response': (r) => {
                    const data = r.json();
                    return Array.isArray(data) && data.every(item =>
                        item.id && item.name && typeof item.id === 'number' && typeof item.name === 'string'
                    );
                },
            });
            errorRate.add(!checkResult);
        } else {
            const id = randomItem(restaurantIds);
            console.log("RESTAURANT ID: ", id);
            const res = http.get(`http://backend:8080/api/v1/restaurant/${id}`);
            restaurantResponseTime.add(res.timings.duration);
            const checkResult = check(res, {
                'get restaurant by id status 200': (r) => r.status === 200,
                'get restaurant by id response time': (r) => r.timings.duration < 1000,
                'get restaurant by id has data': (r) => {
                    const data = r.json();
                    return data && data.id === id;
                },
            });
            errorRate.add(!checkResult);
        }
    }

    sleep(1);
}