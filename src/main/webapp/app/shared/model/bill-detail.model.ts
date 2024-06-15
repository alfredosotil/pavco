import dayjs from 'dayjs';
import { IBill } from 'app/shared/model/bill.model';

export interface IBillDetail {
  id?: number;
  uuid?: string | null;
  code?: string;
  description?: string;
  price?: number;
  quantity?: number;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  bill?: IBill | null;
}

export const defaultValue: Readonly<IBillDetail> = {};
