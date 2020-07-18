package com.mmi.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.mmi.demo.R;
import com.mmi.services.api.autosuggest.MapmyIndiaAutosuggest;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {


    private Context mContext;
    private List<ELocation> resultList = new ArrayList<ELocation>();
    ArrayList<ELocation> suggestedList;

    public AutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public ELocation getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.place_list_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).placeName);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<ELocation> suggestions = getSuggestions(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<ELocation>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given searchText.
     */
    private List<ELocation> getSuggestions(String searchText) {

        /*try {
            AutoSuggestManager autoSuggestManager =new AutoSuggestManager();

            return autoSuggestManager.getSuggestions(searchText, null,false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/

        new MapmyIndiaAutosuggest.Builder()
                .setQuery(searchText)
                .build()
                .enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
                    @Override
                    public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                suggestedList = response.body().getSuggestedLocations();
                                //   if (suggestedList.size() > 0) {
                                    /*recyclerView.setVisibility(View.VISIBLE);
                                    AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, new AutoSuggestAdapter.PlaceName() {
                                        @Override
                                        public void nameOfPlace(String name) {
                                            selectedPlace.selectedPlaceName(name);
                                            recyclerView.setVisibility(View.GONE);
                                 //       }
                                    });
                                    recyclerView.setAdapter(autoSuggestAdapter);*/

                            }
                        } else {
                            Toast.makeText(mContext, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) {
                        System.out.println(t.toString());
                    }
                });

        return suggestedList;
    }

}

