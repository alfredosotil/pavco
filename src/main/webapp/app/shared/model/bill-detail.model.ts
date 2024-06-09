import { IBill } from 'app/shared/model/bill.model';

export interface IBillDetail {
  id?: number;
  uuid?: string | null;
  code?: string;
  description?: string;
  price?: number;
  quantity?: number;
  bill?: IBill | null;
}

export const defaultValue: Readonly<IBillDetail> = {};
