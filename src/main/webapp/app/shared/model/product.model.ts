import { IProductType } from 'app/shared/model/product-type.model';

export interface IProduct {
  id?: number;
  uuid?: string | null;
  code?: string;
  price?: number;
  discount?: number;
  productType?: IProductType | null;
}

export const defaultValue: Readonly<IProduct> = {};
