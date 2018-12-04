
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './ToolBar.css';
import { Grid, Cell} from 'react-md'; 
import ImageGallery from 'react-image-gallery';
import 'react-image-gallery/styles/css/image-gallery.css';
import 'react-image-gallery/styles/scss/image-gallery.scss';
import 'react-image-gallery/styles/css/image-gallery-no-icon.css';
import { withStyles } from '@material-ui/core/styles';
import SwipeableViews from 'react-swipeable-views';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';



function TabContainer({ children, dir }) {
    return (
      <Typography component="div" dir={dir} style={{ padding: 8 * 3 }}>
        {children}
      </Typography>
    );
  }
  
  TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
    dir: PropTypes.string.isRequired,
  };
  


  const styles = theme => ({
    root: {
        flexGrow: 1,
        width: '100vw',
    },
    tabsIndicator: {
        backgroundColor: '#d50000',
      },
      tabRoot: {
        textTransform: 'initial',
        minWidth: 100,
        textColor: '#ffffff',
        fontWeight: theme.typography.fontWeightRegular,
        
        fontFamily: [
          '-apple-system',
          'BlinkMacSystemFont',
          'Akhbar MT',
        ].join(','),
        fontSize: '20px',
        '&:hover': {
          color: '#ffffff',
          opacity: 1,
        },
        '&$tabSelected': {
          color: '#ffffff',
          fontWeight: 'bold',
        },
        '&:focus': {
          color: '#ffffff',
        },
      },
      colorSecondary: {
        backgroundColor: 'rgba(76, 175, 80, 0.0)',
        color: "#ffffff"
      },
      textColorPrimary: {
        
        color: "#ffffff"
      },
      elevationRemoval: {
          elevation: 0,
      }
  });

    

class ToolBar extends Component {
    state = {
        value: 0,
      };
    
      handleChange = (event, value) => {
        this.setState({ value });
      };
    
      handleChangeIndex = index => {
        this.setState({ value: index });
      };
    
      render() {
        const { classes, theme} = this.props;
        const images = [
            {
              srcSet: "irt-student-login.jpg",
                thumbnail: 'irt-student-login.jpg',
                thumbnailLabel: "Student Login"
            },
            {
              srcSet: "irt-student-portal.jpg",
              thumbnail: 'irt-student-portal.jpg',
              thumbnailLabel: "Student Portal"
            },
            {
              srcSet: "irt-student-createreport.jpg",
              thumbnail: 'irt-student-createreport.jpg',
              thumbnailLabel: "Create Report"
            },
            {
              srcSet: "irt-student-selectcategories.jpg",
              thumbnail: 'irt-student-selectcategories.jpg',
              thumbnailLabel: "Create Report Categories"
            },
            {
              srcSet: "irt-student-report-preview.jpg",
              thumbnail: 'irt-student-report-preview.jpg',
              thumbnailLabel: "Student Report Preview"
            },
            {
              srcSet: "irt-student-message.jpg",
              thumbnail: 'irt-student-message.jpg',
              thumbnailLabel: "Student Message"
            },  
            {
              srcSet: "irt-admin-login.jpg",
              thumbnail: 'irt-admin-login.jpg',
              thumbnailLabel: "Admin Login"
            },
            {
              srcSet: "irt-admin-signup.jpg",
              thumbnail: 'irt-admin-signup.jpg',
              thumbnailLabel: "Admin Signup"
            }, 
            {
              srcSet: "irt-admin-join-existing.jpg",
              thumbnail: 'irt-admin-join-existing.jpg',
              thumbnailLabel: "Join Existing Group"
            }, 
            {
              srcSet: "irt-admin-create-group.jpg",
              thumbnail: 'irt-admin-create-group.jpg',
              thumbnailLabel: "Create Group"
            },  
            {
              srcSet: "irt-admin-portal-alert.jpg",
              thumbnail: 'irt-admin-portal-alert.jpg',
              thumbnailLabel: "Admin Alerts"
            },
            {
              srcSet: "irt-admin-portal-reportcards.jpg",
              thumbnail: 'irt-admin-portal-reportcards.jpg',
              thumbnailLabel: "Admin Reports"
            },
            {
              srcSet: "irt-admin-portal-repordcards-filter.jpg",
              thumbnail: 'irt-admin-portal-repordcards-filter.jpg',
              thumbnailLabel: "Filter Reports"
            },  
            {
              srcSet: "irt-admin-report-details1.jpg",
              thumbnail: 'irt-admin-report-details1.jpg',
              thumbnailLabel: "Admin Report Details Pt1"
            },
            {
              srcSet: "irt-admin-reportdetails2.jpg",
              thumbnail: 'irt-admin-reportdetails2.jpg',
              thumbnailLabel: "Admin Report Details Pt2"
            }, 
            {
              srcSet: "irt-admin-report-details-message.jpg",
              thumbnail: 'irt-admin-report-details-message.jpg',
              thumbnailLabel: "Admin Report Message"
            },
            {
              srcSet: "irt-admin-portal-dashboard.jpg",
              thumbnail: 'irt-admin-portal-dashboard.jpg',
              thumbnailLabel: "Admin Dashboard"
            }   
          ]

        return (
          <div className={classes.root}>
            <AppBar position="static" color="secondary" classes={{root: classes.elevationRemoval, colorSecondary: classes.colorSecondary}}>
              <Tabs fixed className="theTabs"
                value={this.state.value}
                onChange={this.handleChange}
                indicatorColor="primary"
                textColor="inherit"
                fullWidth
                classes={{ root: classes.tabsRoot, indicator: classes.tabsIndicator}}
              >
                <Tab label="About Bertha IRT" className="theTabbb"
                 classes={{ root: classes.tabRoot, selected: classes.tabSelected,  textColorPrimary: classes.textColorPrimary}}/>
                <Tab label="Demo" 
                classes={{ root: classes.tabRoot, selected: classes.tabSelected,  textColorPrimary: classes.textColorPrimary }}/>
                <Tab label="Pictures" 
                classes={{ root: classes.tabRoot, selected: classes.tabSelected,  textColorPrimary: classes.textColorPrimary }}/>
                <Tab label="Deliverables"
                classes={{ root: classes.tabRoot, selected: classes.tabSelected,  textColorPrimary: classes.textColorPrimary }}/>
              </Tabs>
            </AppBar>
            <SwipeableViews
              axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
              index={this.state.value}
              onChangeIndex={this.handleChangeIndex}
            >
              <TabContainer dir={theme.direction}><Grid>
                <Cell size={12}>
                        <br></br><br></br><br></br>
                        <br></br><br></br><br></br>
                        <br></br>
                        <Grid>
                          <Cell size={7}>
                          <div className="thisHeaderText">What is Bertha IRT? </div>
                          <br></br>
                          
                          <div className="thisText">BERTHA IRT is a tool which Students, Parents, and Teachers can use to report an incident anonymously.
                              The goal of IRT is to STOP or PREVENT an incident as quickly as possible, while protecting the identity of the Student, Parent, or Teacher.
                              </div>
                          </Cell>
                          
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                       
                          <Cell size={7}>
                          <div className="thisHeaderText">How Does it Work? </div>
                          <br></br>
                          <div className="thisText">Bertha IRT works differently depending on the role a user plays. <br></br><br></br><u>For Students</u>, they can use a code given to them by an Administrator.
                          This code is used to log in to their account. When logged in, the Student can choose to Create a Report or View Report History. Creating a Report is what a Student will do
                          when they want to Report an Incident. View Report History allows the Student to view previously sent Reports which they can click on to show the Student details about their Report, as well as allow them to message back-and-forth with the Administators
                           to include additional details or respond to an Administrators message.<br></br><br></br><u>For Administrators</u>, they can sign up to become a group owner, or log in to an account that is already created. If an Administrator signs up to become a group owner,
                            they will receive an auto-generated code that they will be responsible for giving to Students, Parents, and Teachers for them to register to that group. Administrators can also Invite/Remove Admins to help manage Reports in their group, Change the Intitution Logo,
                            Manage Reports, Message Students, and more.</div>
                          </Cell>
                          <Cell size={5}>
                          <div align="center" className="imageMovement">
                              <img src='studentParentProtection.png' alt="parentStudentprotection" height="270" width="270"/>
                          </div>
                          </Cell>
                       </Grid>
                    </Cell> 
                </Grid></TabContainer>
              <TabContainer dir={theme.direction}><Grid>
                  <Cell size={12}>
                            <br></br><br></br><br></br>
                            <br></br><br></br><br></br>
                            <br></br>
                            <div align="center">
                            <iframe title="websiteVideo" width="720" height="480" src="https://www.youtube.com/embed/rJC7B-9ZfhE" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
                            </div>
                            </Cell>
                 </Grid></TabContainer>
              <TabContainer dir={theme.direction}><Grid>
                    <Cell size={12}>
                        <br></br><br></br><br></br>
                        <br></br><br></br><br></br>
                        <br></br>
                        <div align="center">
                        <ImageGallery showPlayButton={false} showFullscreenButton={false} items={images}></ImageGallery>
                        </div>
                    </Cell>
                </Grid></TabContainer>
                <TabContainer dir={theme.direction}><Grid>
                    <Cell size={12}>
                        <br></br><br></br><br></br>
                        <br></br><br></br><br></br>
                        <br></br>
                        <Grid>
                          <Cell size={7}>
                            <div className="thisHeaderText">Git Repositories: </div>
                            <br></br>
                            <div className="thisText"><p>
                                <a className="thisHyperText" href="https://github.com/BerthaIRT"
                                  title="BERTHA IRT Github Repository" >BERTHA IRT Github</a>.
                                  </p></div>
                          </Cell>
                        </Grid>
                            <br></br>
                            <br></br>
                            <br></br>
                            <br></br>
                        <Grid>
                          <Cell size={7}>
                            <div className="thisHeaderText">Documentation:</div>
                            <br></br>
                            <div className="thisText">List of Documentation...</div>
                          </Cell>
                        </Grid>
                    </Cell>
                </Grid></TabContainer>
            </SwipeableViews>
          </div>
        );
      }
}

ToolBar.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
  };


  export default withStyles(styles, { withTheme: true })(ToolBar);