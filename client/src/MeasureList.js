import React, { Component } from 'react';
import Button from '@material-ui/core/Button';
import CircularProgress from '@material-ui/core/CircularProgress';

class MeasureList extends Component {

  render() {
    const { measuresList } = this.props;

    return <div className='measure-list' data-testid='measure-list'>
      <Button onClick={async () => await this.props.addMeasure()}>
        + Measure
      </Button>
      {measuresList && measuresList.sort().map((measureItem) => {
        const { measureId, measureName, selected, jobStatus } = measureItem;

        let progress;

        if (measureItem.jobStatus === 'RUNNING' && measureItem.progress === 0) {
          progress = <CircularProgress className={`circular-progress ${jobStatus ? jobStatus : 'IDLE'}`} />
        } else {
          progress = <CircularProgress
            className={`circular-progress ${jobStatus ? jobStatus : 'IDLE'}`}
            variant="determinate"
            value={jobStatus === 'RUNNING' ? measureItem.progress : 100}
          />
        }
        return <li
          key={measureId}
          data-testid={`measure-id-${measureId}`}
          className={selected ? 'selected' : ''}
          onClick={(e) => {
            this.props.getMeasure(measureId);
            this.props.selectMeasure(measureId, e);
          }}>
          <span>{measureName}</span>
          {progress}
        </li>
      })}
    </div>
  }
}

export default MeasureList
