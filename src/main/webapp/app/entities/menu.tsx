import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product-type">
        <Translate contentKey="global.menu.entities.productType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/equivalent">
        <Translate contentKey="global.menu.entities.equivalent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bill">
        <Translate contentKey="global.menu.entities.bill" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bill-detail">
        <Translate contentKey="global.menu.entities.billDetail" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bill-file">
        <Translate contentKey="global.menu.entities.billFile" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
