package com.codepath.peterhe.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;
    private String mUsername;
    private static final int MESSAGE_OUTGOING = 27;
    private static final int MESSAGE_INCOMING = 38;
    // Create a gravatar image based on the hash value obtained from userId
    private static void getProfile(final String userId, Context mContext, ImageView imageOther,TextView name) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(ParseUser.KEY_OBJECT_ID);
        query.setLimit(1);
        query.whereEqualTo(ParseUser.KEY_OBJECT_ID,userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    //Toast.makeText(, "Error getting groups", Toast.LENGTH_SHORT).show()
                    e.printStackTrace();
                } else {
                    if (user != null) {
                        ParseFile image = user.getParseFile("profile");
                        Glide.with(mContext)
                                .load(image.getUrl())
                                .circleCrop() // create an effect of a round profile picture
                                .into(imageOther);

                        name.setText(user.getUsername());
                    }
                }
            }
        });


    }

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }
    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther_incomingDetailChat);
            body = (TextView)itemView.findViewById(R.id.tvBody_incomingDetailChat);
            name = (TextView)itemView.findViewById(R.id.tvName_incomingDetailChat);
        }

        @Override
        public void bindMessage(Message message) {
            getProfile(message.getUserId(),mContext,imageOther,name);
            body.setText(message.getBody());
             // in addition to message show user ID
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView body;
        TextView name;


        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe_outgoingDetailChat);
            body = (TextView)itemView.findViewById(R.id.tvBody_outgoingDetailChat);
            name = (TextView)itemView.findViewById(R.id.tvName_outgoingDetailChat);
        }

        @Override
        public void bindMessage(Message message) {
            getProfile(message.getUserId(),mContext,imageMe,name);
            body.setText(message.getBody());
        }
    }


    // TODO: create onCreateViewHolder and onBindViewHolder later (covered in the next few chapters...)

}
