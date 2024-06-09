import { IClient } from 'app/shared/model/client.model';

export interface IBill {
  id?: number;
  uuid?: string | null;
  code?: string;
  notes?: string | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IBill> = {};
