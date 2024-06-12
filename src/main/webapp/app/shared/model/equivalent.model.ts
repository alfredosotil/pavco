import { IProduct } from 'app/shared/model/product.model';
import { IClient } from 'app/shared/model/client.model';

export interface IEquivalent {
  id?: number;
  uuid?: string | null;
  code?: string;
  name?: string;
  price?: number;
  discount?: number;
  product?: IProduct | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IEquivalent> = {};
