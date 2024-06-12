export interface IProductType {
  id?: number;
  uuid?: string | null;
  code?: string;
  name?: string;
  price?: number;
  discount?: number;
}

export const defaultValue: Readonly<IProductType> = {};
