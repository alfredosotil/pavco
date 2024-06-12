import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './equivalent.reducer';

export const EquivalentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const equivalentEntity = useAppSelector(state => state.equivalent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="equivalentDetailsHeading">
          <Translate contentKey="pavcoApp.equivalent.detail.title">Equivalent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.equivalent.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.uuid}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="pavcoApp.equivalent.code">Code</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="pavcoApp.equivalent.name">Name</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="pavcoApp.equivalent.price">Price</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.price}</dd>
          <dt>
            <span id="discount">
              <Translate contentKey="pavcoApp.equivalent.discount">Discount</Translate>
            </span>
          </dt>
          <dd>{equivalentEntity.discount}</dd>
          <dt>
            <Translate contentKey="pavcoApp.equivalent.product">Product</Translate>
          </dt>
          <dd>{equivalentEntity.product ? equivalentEntity.product.code : ''}</dd>
          <dt>
            <Translate contentKey="pavcoApp.equivalent.client">Client</Translate>
          </dt>
          <dd>{equivalentEntity.client ? equivalentEntity.client.ruc : ''}</dd>
        </dl>
        <Button tag={Link} to="/equivalent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/equivalent/${equivalentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EquivalentDetail;
