import axios from 'axios';

const API_BASE_URL = 'http://localhost';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth interceptor
api.interceptors.request.use((config) => {
  // Only add auth header on client side
  if (typeof window !== 'undefined') {
    try {
      
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        config.headers.Authorization = `Basic ${btoa(`${userData.username}:${userData.password}`)}`;
      }
    } catch (error) {
      console.error('Error setting auth header:', error);
    }
  }
  return config;
});

// Types
export interface Restaurant {
  id?: number;
  name: string;
  location: string;
  seats: number;
}

export interface Meal {
  id?: number;
  restaurant: Restaurant;
  price: number;
  date: string;
  name: string;
}

export interface User {
  id?: number;
  username: string;
  password: string;
  role: 'USER' | 'STAFF';
}

export interface UserMeal {
  id?: number;
  user: User;
  meal: Meal;
  isCheck: boolean;
  code: string;
}

export interface WeatherIPMA {
  tempMin: string;
  tempMax: string;
  windDirection: string;
  precipitationProb: string;
  typeOfWeatherId: number;
  precipitationIntensity: number;
  iuv: string;
}

export interface MealDTO {
  meal: Meal;
  weatherIPMA: WeatherIPMA;
}

export interface UserMealDTO {
  weatherIPMA: WeatherIPMA;
  userMeal: UserMeal;
} 