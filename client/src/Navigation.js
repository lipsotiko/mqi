import React, { Component } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Grid from '@material-ui/core/Grid';

class Navigation extends Component {
  render() {
    return <nav>
      <AppBar style={{ background: '#282c34' }}>
        <Grid
          justify="space-between"
          container>
          <Grid item>
            <Tabs
              data-testid='navigation'
              value={this.props.currentTab} onChange={(e,v) => this.props.setTab(e,v)}>
              <Tab data-testid='reporting' label='Reports' />
              <Tab data-testid='measure-editor' label='Editor' />
            </Tabs>
          </Grid>
          <Grid item>
            <div className='right-nav-buttons'>
              {this.props.rightButtons}
            </div>
          </Grid>
        </Grid>
      </AppBar>
    </nav>
  }
}

export default Navigation;
