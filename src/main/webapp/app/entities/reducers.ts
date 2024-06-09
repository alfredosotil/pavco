import product from 'app/entities/product/product.reducer';
import productType from 'app/entities/product-type/product-type.reducer';
import equivalent from 'app/entities/equivalent/equivalent.reducer';
import bill from 'app/entities/bill/bill.reducer';
import billDetail from 'app/entities/bill-detail/bill-detail.reducer';
import client from 'app/entities/client/client.reducer';
import billFile from 'app/entities/bill-file/bill-file.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  productType,
  equivalent,
  bill,
  billDetail,
  client,
  billFile,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
