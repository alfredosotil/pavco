import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-type.reducer';

export const ProductTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productTypeEntity = useAppSelector(state => state.productType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productTypeDetailsHeading">
          <Translate contentKey="pavcoApp.productType.detail.title">ProductType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.productType.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.uuid}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="pavcoApp.productType.code">Code</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="pavcoApp.productType.name">Name</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="pavcoApp.productType.price">Price</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.price}</dd>
          <dt>
            <span id="discount">
              <Translate contentKey="pavcoApp.productType.discount">Discount</Translate>
            </span>
          </dt>
          <dd>{productTypeEntity.discount}</dd>
        </dl>
        <Button tag={Link} to="/product-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-type/${productTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductTypeDetail;
