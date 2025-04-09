import { api } from './api';
import { UserMeal, UserMealDTO } from './api';

export const userMealService = {
  getAllByUserId: async (userId: number) => {
    const response = await api.get<UserMealDTO[]>(`/api/private/v1/user-meal/all/${userId}`);
    return response.data;
  },

  getAllByRestaurantId: async (restaurantId: number) => {
    const response = await api.get<UserMealDTO[]>(`/api/private/v1/user-meal/all/restaurant/${restaurantId}`);
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<UserMealDTO>(`/api/private/v1/user-meal/${id}`);
    return response.data;
  },

  create: async (userMeal: Omit<UserMeal, 'id'>) => {
    console.log(userMeal);
    const response = await api.post<UserMeal>('/api/private/v1/user-meal', userMeal);
    return response.data;
  },

  update: async (userMeal: UserMeal) => {
    const response = await api.put<UserMeal>('/api/private/v1/user-meal', userMeal);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/api/private/v1/user-meal/${id}`);
  }
}; 