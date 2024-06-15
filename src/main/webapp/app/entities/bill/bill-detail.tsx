import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
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
            <span id="total">
              <Translate contentKey="pavcoApp.bill.total">Total</Translate>
            </span>
          </dt>
          <dd>{billEntity.total}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="pavcoApp.bill.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{billEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="pavcoApp.bill.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{billEntity.createdDate ? <TextFormat value={billEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="pavcoApp.bill.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{billEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="pavcoApp.bill.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {billEntity.lastModifiedDate ? <TextFormat value={billEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
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
