import { api } from './api';
import { User } from './api';

export const userService = {
  getById: async (id: number) => {
    const response = await api.get<User>(`/api/private/v1/users/${id}`);
    return response.data;
  },

  getByName: async (name: string) => {
    const response = await api.get<User>(`/api/private/v1/users/name/${name}`);
    return response.data;
  },

  create: async (user: Omit<User, 'id'>) => {
    const response = await api.post<User>('/api/v1/users', user);
    return response.data;
  },

  update: async (user: User) => {
    const response = await api.put<User>('/api/private/v1/users', user);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/api/private/v1/users/${id}`);
  }
}; 