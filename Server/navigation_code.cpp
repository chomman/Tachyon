#include<bits/stdc++.h>
/*-------------------------------------------------------- */
using namespace std;

/*-------------------------------------------------------- */

#define rep(i,val,n) for(ll i=val;i<n;i++)
#define per(j,val,n) for(ll j=val;j>=n;j--)
#define pb push_back
#define pi 3.14159265
#define mp make_pair
#define MODULO 1000000007
#define INF 1000000000000000
#define fastread ios_base::sync_with_stdio(false); cin.tie(NULL);cout.tie(NULL);
#define x first
#define y second
#define SZ(x) ((int)(x).size())
/*-------------------------------------------------------- */
typedef long double ld;
typedef long long ll;
typedef vector<bool> boolean;
typedef vector<ll> vec;


/*-------------------------------------------------------- */

ll gcd(ll a, ll b)
{
    if(b == 0)
    {
        return a;
    }
    return gcd(b, a%b);
}

ll lcm(ll a, ll b)
{
    return ((a*b)/gcd(a,b));
}

/*bool comp(const pair<int,int> &a,
              const pair<int,int> &b)
{
    if(a.first!=b.first)
        return(a.first<b.first);
        else
            return (a.second>b.second); //comp of vector of pairs

} */
 // priority_queue <ll, vector<ll>, greater<ll> > q; //min heap
/*---------------------------------------------------------*/
//r q d



struct pnode
{
    ll x;
    ll y;
};

int main()
{
   ll n,m;
   cin>>n>>m;
   ll grid[n][m];
   pnode parent[n][m];
   ll visited[n][m];

   rep(i,0,n)
       rep(j,0,m)
           cin>>grid[i][j];


    map< string , pair<ll,ll> > mapping;
    ll k1;
    cin>>k1;
    rep(i,0,k1)
    {
        string s1;
        ll x,y;
        cin>>s1;
        cin>>x>>y;
        mapping[s1] = {x,y};
    }




    memset(visited,0,sizeof(visited));

    string star,dest;
    cin>>dest>>star;
    ll x1,y1,x2,y2;
    queue< pair<ll,ll> > q;
    //cout<<"enter the source node coordinates"<<endl;
    x1 = mapping[star].x;
    y1 = mapping[star].y;
    //cout<<"enter destination node coordinates"<<endl;
    x2 = mapping[dest].x;
    y2 = mapping[dest].y;

    visited[x1][y1]=1;

    rep(i,0,n)
    {
    rep(j,0,m)
    {
    parent[i][j].x=-1;
    parent[i][j].y=-1;
    }
    }

    //cout<<endl;


    q.push({x1,y1});
    while(q.size()>0)
    {
        pair<ll,ll> p = q.front();
        q.pop();
        if(grid[p.x-1][p.y]==1&&visited[p.x-1][p.y]==0&&p.x-1>=0)
        {
            q.push({p.x-1,p.y});
            parent[p.x-1][p.y].x=p.x;
            parent[p.x-1][p.y].y=p.y;
            visited[p.x-1][p.y]=1;
        }
        if(grid[p.x+1][p.y]==1&&visited[p.x+1][p.y]==0&&p.x+1<n)
        {
            q.push({p.x+1,p.y});
            parent[p.x+1][p.y].x=p.x;
            parent[p.x+1][p.y].y=p.y;
             visited[p.x+1][p.y]=1;

        }

        if(grid[p.x][p.y+1]==1&&visited[p.x][p.y+1]==0&&p.y+1<m)
        {
            q.push({p.x,p.y+1});
            parent[p.x][p.y+1].x=p.x;
            parent[p.x][p.y+1].y=p.y;
             visited[p.x][p.y+1]=1;
        }

        if(grid[p.x][p.y-1]==1&&visited[p.x][p.y-1]==0&&p.y-1>=0)
        {
            q.push({p.x,p.y-1});
            parent[p.x][p.y-1].x=p.x;
            parent[p.x][p.y-1].y=p.y;
             visited[p.x][p.y-1]=1;
        }


    }

   /* rep(i,0,n)
    rep(j,0,m)
    {
        if(grid[i][j]==1)
    cout<<i<<" "<<j<<" "<<parent[i][j].x<<" "<<parent[i][j].y<<endl;
    }*/

      //cout<<parent[3][4].x<<" "<<parent[3][4].y<<endl;


    vector< pair<ll,ll> > vstack;


    ll changex,changey;
    ll currx=x2,curry=y2;
    while(currx!=-1&&curry!=-1)
    {

        vstack.push_back({currx,curry});
        ll temp1=currx,temp2=curry;
        currx=parent[temp1][temp2].x;
        curry=parent[temp1][temp2].y;
        //cout<<currx<<" "<<curry<<" "<<parent[currx][curry].x<<" "<<parent[currx][curry].y<<endl;
        if(vstack.size()==2)
        {
            changex = vstack[vstack.size()-1].x-vstack[vstack.size()-2].x;
            changey = vstack[vstack.size()-1].y-vstack[vstack.size()-2].y;
        }
        else if(vstack.size()>2)
        {
            ll d1,d2;
            d1 = vstack[vstack.size()-1].x-vstack[vstack.size()-2].x;
            d2 = vstack[vstack.size()-1].y-vstack[vstack.size()-2].y;

            if(d1==changex&&d2==changey)
            {
                vstack[vstack.size()-2]=vstack[vstack.size()-1];
                vstack.pop_back();
            }
            else
            {
                changex=d1;
                changey=d2;
            }


        }
    }
    //cout<<endl<<endl;
    ll configure = vstack.size()- 1;
    per(i,vstack.size()-1,configure)
    {
        //cout<<vstack[i].x<<" "<<vstack[i].y<<" --------> "<<vstack[i-1].x<<" "<<vstack[i-1].y;
        ll d1,d2;
            d1 = vstack[i-1].x-vstack[i].x;
            d2 = vstack[i-1].y-vstack[i].y;

            if(d2>0)
            cout<<"Right"<<endl;
            else if(d2<0)
            cout<<"Left"<<endl;
            else if(d1>0)
            cout<<"Back"<<endl;
            else if(d1<0)
            cout<<"Straight"<<endl;
            if(star!=dest)
                cout<<abs(vstack[i].x + vstack[i].y - vstack[i-1].x - vstack[i-1].y)*10<<endl;
	    else
	    	cout<<0<<endl;

        }





    return 0;
}


/*--------------------------------------------------------------------*/
