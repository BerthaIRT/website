import React, { Component } from 'react';
import './App.css';
import ToolBar from './ToolBar.js';
import { Grid, Cell} from 'react-md'; 


class App extends Component {
  render() {
    

    return (
      <Grid>
        <img src='BerthaIRT-logo.png' alt="universityOfAl" height="90" width="80" class="fadeImage"/>  
        <ToolBar></ToolBar>
      </Grid>
    );
  }
}
export default App;
