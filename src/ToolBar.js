
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
        textColor: '#000000',
        fontWeight: theme.typography.fontWeightRegular,
        
        fontFamily: [
          '-apple-system',
          'BlinkMacSystemFont',
          'Akhbar MT',
        ].join(','),
        fontSize: '20px',
        '&:hover': {
          color: '#000000',
          opacity: 1,
        },
        '&$tabSelected': {
          color: '#000000',
          fontWeight: 'bold',
        },
        '&:focus': {
          color: '#000000',
        },
      },
      colorSecondary: {
        backgroundColor: 'rgba(76, 175, 80, 0.0)',
        color: "#000000"
      },
      textColorPrimary: {
        
        color: "#000000"
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
                          <div className="thisText">BERTHA IRT works differently depending on the role a user plays. <br></br><br></br><u>For Students</u>, they can use a code given to them by an Administrator. This code is used to log in to their account. When logged in, students will be greeted to a screen containing the following:
                          <br></br>
                          <br></br>
                          <Cell size={9} className="tabTexts">
                          - <i>The Institution Logo</i>
                          <br></br>
                          <br></br>
                          - <i>My Reports</i> - Features the ability to view previously submitted reports as well as the report's details. Inside a report, a Student have the ability to see the current Status of a report. Students are also given the ability to send messages to the Administrators to add additional information or respond to messages sent by the Administrators.
                          <br></br>
                          <br></br>
                          - <i>Create New Report</i> - Allows the Student to create a report by filling out options such as: threat level, when it happened, where it happened, what happened, as well as give them the ability to attach media in the event of cyberbullying or inappropriate pictures being sent.
                          <br></br>
                          <br></br>
                          <br></br>
                          </Cell>
                          <u>For Administrators</u>, they can sign up to become a group owner, join an existing group as administrator, or log in to an account that is already created. If an Administrator signs up to become a group owner they will receive an auto-generated code that they will be responsible for giving to Students, Parents, and Teachers for them to register to that group. When logged in, Administrators are greeted to the Admin Portal which contains the following:
                          <br></br>
                          <br></br>
                          
                          <Cell size={9} className="tabTexts">
                          - <i>Alerts Tab</i> - Administrators are given the ability to view Alerts such as: Student messages, a Report Status is changed, Student submits a new Report, and more!
                          <br></br>
                          <br></br>
                          - <i>Reports Tab</i> - Administrators have the ability to Filter and Search Reports. They can also Manage Reports through the viewing of Report Details, Edit Report Details, Assign themselves to a Report, and more!
                          <br></br>
                          <br></br>
                          - <i>Dashboard Tab</i> - Administrators have the ability to Edit their Institution Name, Institution Emblem, and their personal Name. They can also Close/Open Registration and Add/Remove other Administrators to their group.
                          </Cell>
                          </div>
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
                        <br></br>
                            <div align="center">
                            <iframe title="websiteVideo" width="720" height="480" src="https://www.youtube.com/embed/MXnWmxlCW6g" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
                            </div>
                            </Cell>
                 </Grid></TabContainer>
              <TabContainer dir={theme.direction}><Grid>
                    <Cell size={12}>
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
                        <br></br>
                        <Grid>
                          <Cell size={7}>
                            <div className="thisHeaderText">Github Repository: </div>
                            <br></br>
                            <div className="thisText"><p>
                                <a className="thisHyperText" href="https://github.com/BerthaIRT"
                                  title="BERTHA IRT Github Repository" >BERTHA IRT Github</a>.
                                  </p></div>
                            <br></br>
                            <br></br>
                            <div className="thisHeaderText">Downloads: </div>
                            <br></br>
                            <div className="thisText"><p>
                                <a className="thisHyperText" href="http://54.236.113.200/irt.apk"
                                  title="BERTHA IRT APK" >BERTHA IRT APK</a>.
                                  </p></div>
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
