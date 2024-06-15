import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';

export interface IBillFile {
  id?: number;
  uuid?: string | null;
  name?: string;
  size?: number;
  mimeType?: string;
  contentContentType?: string;
  content?: string;
  isProcessed?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  client?: IClient | null;
}

export const defaultValue: Readonly<IBillFile> = {
  isProcessed: false,
};
