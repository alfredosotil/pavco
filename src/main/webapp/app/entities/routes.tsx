import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Product from './product';
import ProductType from './product-type';
import Equivalent from './equivalent';
import Bill from './bill';
import BillDetail from './bill-detail';
import Client from './client';
import BillFile from './bill-file';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="product/*" element={<Product />} />
        <Route path="product-type/*" element={<ProductType />} />
        <Route path="equivalent/*" element={<Equivalent />} />
        <Route path="bill/*" element={<Bill />} />
        <Route path="bill-detail/*" element={<BillDetail />} />
        <Route path="client/*" element={<Client />} />
        <Route path="bill-file/*" element={<BillFile />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
