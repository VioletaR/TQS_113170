import { api } from './api';
import { Meal, MealDTO } from './api';

export const mealService = {
  getAllByRestaurantId: async (restaurantId: number) => {
    const response = await api.get<MealDTO[]>(`/api/v1/meals/all/${restaurantId}`);
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<MealDTO>(`/api/v1/meals/${id}`);
    return response.data;
  },

  create: async (meal: Omit<Meal, 'id'>) => {
    const response = await api.post<Meal>('/api/v1/meals', meal);
    return response.data;
  },

  update: async (meal: Meal) => {
    const response = await api.put<Meal>('/api/v1/meals', meal);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/api/v1/meals/${id}`);
  }
}; 