/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import ReactMoE, {MoEProperties} from 'react-native-moengage'
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  Linking,
  Alert
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';
import { APP_ID } from 'react-native-moengage/src/utils/MoEConstants';

/* $FlowFixMe[missing-local-annot] The type annotation(s) required by Flow's
 * LTI update could not be added via codemod */
const Section = ({children, title}): Node => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
};

class App extends React.Component {
  constructor(props) {
    super(props)

    this.state =  { color : 'lightgreen' };
  }

  render() {

    let APP_ID = "8SIW681S80Z08KSHQFSTIZ8T"
    ReactMoE.initialize(APP_ID)
    ReactMoE.showInApp()
    
    ReactMoE.setEventListener("pushTokenGenerated", (payload) => {
      console.log("MoE pushTokenGenerated", payload)
    })

    ReactMoE.setEventListener("pushClicked", (notificationPayload) => {
      console.log("MoE pushClicked", notificationPayload)
    })

    ReactMoE.setEventListener("inAppCampaignClicked", async (inAppInfo) => {
      console.log("MoE inAppClicked", inAppInfo)

      if(inAppInfo.action != null && inAppInfo.action.actionType == "navigation" && inAppInfo.action.navigationType == "deep_linking"){
        let url = inAppInfo.action.navigationUrl

        if(Linking.canOpenURL(url)){
          console.log("MoE inAppClicked redirection", url)
          await Linking.openURL(url)
        } else {
          console.log("No activity found to handle deeplink")
        }
      }
    })

    return (
      <View style={{flexDirection:'column', gap:10}}>
          <Text
            style={{
              color: this.state.color,
              backgroundColor: 'rgba(0,0,0,0.88)',
              textAlign: 'center',
              paddingTop: 20,
              width: 400,
              height: 80,
              margin: 'auto'
            }}
          >
            React Native JS App
          </Text>

          <Button
            title="Enable Ad Id Tracking"
            onPress={() => ReactMoE.enableAdIdTracking()}
          />

          <Button
            title='disable Ad Id Tracking'
            onPress={ () => ReactMoE.disableAdIdTracking()}
            />

            <Button
            title='Login'
            onPress={ () => ReactMoE.setUserUniqueID("react@native_user.com")}
            />

            <Button
            title='Logout'
            onPress={ () => ReactMoE.logout()}
            />

            <Button
            title='Set user name'
            onPress={ () => ReactMoE.setUserName('SuperName 2')} />

            <Button
            title='Set alias'
            onPress={ () => ReactMoE.setAlias('123 Set alias')} />

            <Button
            title='set first name'
            onPress={ () => ReactMoE.setUserFirstName("FName")} />

            <Button
            title='Set last name'
            onPress={ () => ReactMoE.setUserLastName('Lname')} />

            <Button
            title='Set contact'
            onPress={ () => ReactMoE.setUserContactNumber(1234567890)} />

            <Button
            title='Set phone sale'
            onPress={ () => {
              let props = new MoEProperties();
              props.addAttribute("quantity", 1)
              props.addAttribute("name", "iPhone")

              ReactMoE.trackEvent("Phone sale", props)
            }} />

            <Button
            title='Open Settings'
            onPress={ async () => {
              await Linking.openSettings()
            }} />

            <Button
            title='Open Maps with linking'
            onPress={ async () => {
              await Linking.openURL("geo:37.484847,-122.148386")
            }} />

            <Button
            title='Open fufu'
            onPress={ async () => {
              await Linking.openURL("fufu://fufu")
            }} />


            <Button
            title='Send Intent'
            onPress={ async () => {
              
              action = 'android.intent.action.VIEW'

              extras=[
                {
                  key: 'android.provider.extra.APP_PACKAGE',
                  value: 'akudo.technologies',
                }]

              await Linking.sendIntent(action, extras)

            }} />

            
        </View>
    );
  }
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
