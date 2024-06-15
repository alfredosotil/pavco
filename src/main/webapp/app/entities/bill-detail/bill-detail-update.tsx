import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBill } from 'app/shared/model/bill.model';
import { getEntities as getBills } from 'app/entities/bill/bill.reducer';
import { IBillDetail } from 'app/shared/model/bill-detail.model';
import { getEntity, updateEntity, createEntity, reset } from './bill-detail.reducer';

export const BillDetailUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bills = useAppSelector(state => state.bill.entities);
  const billDetailEntity = useAppSelector(state => state.billDetail.entity);
  const loading = useAppSelector(state => state.billDetail.loading);
  const updating = useAppSelector(state => state.billDetail.updating);
  const updateSuccess = useAppSelector(state => state.billDetail.updateSuccess);

  const handleClose = () => {
    navigate('/bill-detail');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBills({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...billDetailEntity,
      ...values,
      bill: bills.find(it => it.id.toString() === values.bill?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...billDetailEntity,
          createdDate: convertDateTimeFromServer(billDetailEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(billDetailEntity.lastModifiedDate),
          bill: billDetailEntity?.bill?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pavcoApp.billDetail.home.createOrEditLabel" data-cy="BillDetailCreateUpdateHeading">
            <Translate contentKey="pavcoApp.billDetail.home.createOrEditLabel">Create or edit a BillDetail</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bill-detail-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('pavcoApp.billDetail.uuid')}
                id="bill-detail-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
                validate={{}}
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.code')}
                id="bill-detail-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 12, message: translate('entity.validation.maxlength', { max: 12 }) },
                  pattern: { value: /^[0-9]+/, message: translate('entity.validation.pattern', { pattern: '^[0-9]+' }) },
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.description')}
                id="bill-detail-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 512, message: translate('entity.validation.maxlength', { max: 512 }) },
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.price')}
                id="bill-detail-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.quantity')}
                id="bill-detail-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.createdBy')}
                id="bill-detail-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.createdDate')}
                id="bill-detail-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.lastModifiedBy')}
                id="bill-detail-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('pavcoApp.billDetail.lastModifiedDate')}
                id="bill-detail-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="bill-detail-bill" name="bill" data-cy="bill" label={translate('pavcoApp.billDetail.bill')} type="select">
                <option value="" key="0" />
                {bills
                  ? bills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bill-detail" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BillDetailUpdate;
