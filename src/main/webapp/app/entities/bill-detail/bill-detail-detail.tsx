import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill-detail.reducer';

export const BillDetailDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billDetailEntity = useAppSelector(state => state.billDetail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billDetailDetailsHeading">
          <Translate contentKey="pavcoApp.billDetail.detail.title">BillDetail</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.billDetail.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.uuid}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="pavcoApp.billDetail.code">Code</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pavcoApp.billDetail.description">Description</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="pavcoApp.billDetail.price">Price</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.price}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="pavcoApp.billDetail.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{billDetailEntity.quantity}</dd>
          <dt>
            <Translate contentKey="pavcoApp.billDetail.bill">Bill</Translate>
          </dt>
          <dd>{billDetailEntity.bill ? billDetailEntity.bill.code : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill-detail" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill-detail/${billDetailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillDetailDetail;
