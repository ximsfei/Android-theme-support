package theme.support.demo.basic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import theme.support.demo.R;

public class BasicWidgetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_widget, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        final CharSequence[] entries = getResources().getStringArray(R.array.languages);
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, entries);
        adapter.setDropDownViewResource(R.layout.simple_list_item);
        spinner.setAdapter(adapter);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.auto);
        String[] arrA = {"aa", "aab", "aac"};
        ArrayAdapter<String> arrayAdapterA = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, arrA);
        autoCompleteTextView.setAdapter(arrayAdapterA);
        autoCompleteTextView.setThreshold(1);

        MultiAutoCompleteTextView multiAutoCompleteTextView = (MultiAutoCompleteTextView) view.findViewById(R.id.multi_auto);
        String[] arrB = {"bb", "bbc", "bbd,cc"};
        ArrayAdapter<String> arrayAdapterB = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, arrB);
        multiAutoCompleteTextView.setAdapter(arrayAdapterB);
        multiAutoCompleteTextView.setThreshold(1);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        return view;
    }
}
