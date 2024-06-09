import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill.reducer';

export const BillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billEntity = useAppSelector(state => state.bill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billDetailsHeading">
          <Translate contentKey="pavcoApp.bill.detail.title">Bill</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.bill.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{billEntity.uuid}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="pavcoApp.bill.code">Code</Translate>
            </span>
          </dt>
          <dd>{billEntity.code}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="pavcoApp.bill.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{billEntity.notes}</dd>
          <dt>
            <Translate contentKey="pavcoApp.bill.client">Client</Translate>
          </dt>
          <dd>{billEntity.client ? billEntity.client.ruc : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill/${billEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillDetail;
