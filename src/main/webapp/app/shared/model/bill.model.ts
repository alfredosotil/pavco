import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';

export interface IBill {
  id?: number;
  uuid?: string | null;
  code?: string;
  notes?: string | null;
  total?: number;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  client?: IClient | null;
}

export const defaultValue: Readonly<IBill> = {};
