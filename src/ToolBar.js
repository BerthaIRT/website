import React, { Component } from 'react';
import './ToolBar.css';
import { TabsContainer, Tabs, Tab, ListItem, MenuButton,
Grid, Cell } from 'react-md';
import ImageGallery from 'react-image-gallery';
import 'react-image-gallery/styles/css/image-gallery.css';
import 'react-image-gallery/styles/scss//image-gallery.scss';
import 'react-image-gallery/styles/css/image-gallery-no-icon.css';
import 'react-image-gallery/styles/scss/image-gallery-no-icon.scss';


export default class ToolBar extends Component {
  render() {
    const images = [
        {
          srcSet: "shelby.jpg",
          media:'(max-width: 500px)',
          thumbnail: 'shelby.jpg',
        },
        {
          original: 'shelby.jpg',
          thumbnail: 'shelby.jpg'
         
        },
        {
          original: 'shelby.jpg',
          thumbnail: 'shelby.jpg'
    
        }
      ]

    return (
      <TabsContainer panelClassName="md-grid" fixed colored defaultTabIndex={0} centered >
            <Tabs tabId="simple-tab">
                    <Tab label="About Bertha">
                        <Grid>
                            <Cell size={12}>
                            <div className="thisText">BERTHA IRT is a tool which Students, Parents, and Teachers can use to report an incident anonymously.
                             Students join groups using an Access Code generated when an administrator creates a group.
                              Administrators are given tools to manage reports and keep the sender updated.</div>
                            </Cell>
                            
                        </Grid>
                    </Tab>
                    <Tab label="Demo">
                        <Grid>
                            <Cell size={12}>
                            <div className="thisText">Bajjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjking Cakes and Cookies and creme and logic</div>
                            </Cell>
                        </Grid>
                    </Tab>
                    <Tab label="Pictures">
                        <Grid>
                            <Cell size={12}>
                                <ImageGallery showPlayButton={false} items={images}></ImageGallery>
                            </Cell>
                        </Grid>
                    </Tab>
                    <Tab label="Deliverables" >
                        <Grid>
                            <Cell size={12}>
                            <div className="thisHeaderText">Git Repositories: </div>
                            <br></br>
                            <div className="thisText">List of Git Repositories...</div>
                            <br></br>
                            <br></br>
                            <br></br>
                            <br></br>
                            <div className="thisHeaderText">Documentation:</div>
                            <br></br>
                            <div className="thisText">List of Documentation...</div>
                            </Cell>

                            
                     
                        </Grid>
                    </Tab>
            </Tabs>
      </TabsContainer>
    )
  }
}
