package com.azalelnation.softmute;

import java.io.IOException;
import java.util.UUID;

public interface DataStore {

    public void loadData() throws IOException;

    public void saveData();

    public boolean isMuted(UUID uuid);

    public boolean toggleMute(UUID uuid);

}
