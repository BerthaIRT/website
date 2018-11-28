package com.ua.cs495f2018.berthaIRT;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ua.cs495f2018.berthaIRT.fragment.AdminReportCardsFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditReport {

    @Rule
    public ActivityTestRule<Client> mActivityTestRule = new ActivityTestRule<>(Client.class);

    @Before
    public void setup() {
        ViewInteraction cardView = onView(
                allOf(withId(R.id.newuser_button_adminlogin),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        cardView.perform(click());

        onView(withId(R.id.adminlogin_input_email)).perform(clearText(), typeText("ssinischo@gmail.com"));
        onView(withId(R.id.adminlogin_input_password)).perform(clearText(), typeText("111111"));

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.adminlogin_button_login),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView2),
                                        0),
                                6),
                        isDisplayed()));
        cardView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void editReport() {
        onView(withId(R.id.adminmain_button_reports)).perform(click());
        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.adminmain_button_reports),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.adminmain_bottomnav),
                                        0),
                                2),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.alertcards_rv),
                        childAtPosition(
                                withId(R.id.container),
                                2)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction cardView3 = onView(
                allOf(withId(R.id.cardviewOpen),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.cardView17),
                                                0)),
                                0),
                        isDisplayed()));
        cardView3.perform(click());

        ViewInteraction cardView4 = onView(
                allOf(withId(R.id.cardviewClosed),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.cardView17),
                                                0)),
                                1),
                        isDisplayed()));
        cardView4.perform(click());

        ViewInteraction cardView5 = onView(
                allOf(withId(R.id.cardviewResolved),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.cardView17),
                                                0)),
                                2),
                        isDisplayed()));
        cardView5.perform(click());

        ViewInteraction cardView6 = onView(
                allOf(withId(R.id.admin_reportdetails_button_viewlog),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView5),
                                        0),
                                8)));
        cardView6.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.log_recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.log_recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction cardView7 = onView(
                allOf(withId(R.id.admin_reportdetails_button_attachments),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                9)));
        cardView7.perform(scrollTo(), click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.admin_reportdetails_button_editcategory),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.checkboxes_rv),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView4.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction cardView8 = onView(
                allOf(withId(R.id.checkboxes_button_confirm),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        cardView8.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.admin_reportdetails_button_edittags),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                6)));
        appCompatImageView2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.addremove_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("ji"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.addremove_input), withText("ji"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.addremove_input), withText("ji"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("jiim"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.addremove_input), withText("jiim"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.addremove_button_add),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.addremove_button_delete),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction cardView9 = onView(
                allOf(withId(R.id.addremove_button_confirm),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                2),
                        isDisplayed()));
        cardView9.perform(click());

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.admin_reportdetails_button_edittags),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                6)));
        appCompatImageView5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.addremove_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("jim"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.addremove_input), withText("jim"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.addremove_input), withText("jim"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("jim"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.addremove_input), withText("jim"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.addremove_button_add),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cardView10),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView6.perform(click());

        ViewInteraction cardView10 = onView(
                allOf(withId(R.id.addremove_button_confirm),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                2),
                        isDisplayed()));
        cardView10.perform(click());

        pressBack();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
