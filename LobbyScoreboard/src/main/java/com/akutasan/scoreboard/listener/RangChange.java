package com.akutasan.scoreboard.listener;

import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateUserEvent;


public class RangChange{
    public void onPermissionUpdate(PermissionUpdateUserEvent e){
        System.out.println("Updated!");
    }
}
